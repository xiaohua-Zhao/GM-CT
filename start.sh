#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
JAVA_EXE='D:\DevelopSoftware\jdks\bin\java.exe'
JAVA_HOME_WIN='D:\DevelopSoftware\jdks'
MAVEN_CMD='D:\DevelopSoftware\maven\apache-maven-3.6.3\bin\mvn.cmd'
NPM_CMD='D:\DevelopSoftware\VUE\nodejs\npm.cmd'
JAR="$ROOT/server/target/fashion-mgmt-server-1.0.0-SNAPSHOT.jar"
WIN_ROOT="$(cygpath -w "$ROOT" 2>/dev/null || printf '%s' "$ROOT")"
WIN_WEB="$(cygpath -w "$ROOT/web" 2>/dev/null || printf '%s' "$ROOT/web")"
SERVER_LOG_DIR="$ROOT/server/logs"
WEB_LOG_DIR="$ROOT/web/logs"

mkdir -p "$SERVER_LOG_DIR" "$WEB_LOG_DIR" "$ROOT/logs"

is_port_listening() {
  netstat -ano 2>/dev/null | grep -E "[:.]$1[[:space:]].*LISTENING" >/dev/null 2>&1
}

if [[ "${1:-}" == "--build" || ! -f "$JAR" ]]; then
  echo "[1/3] 正在构建后端..."
  export JAVA_HOME="$JAVA_HOME_WIN"
  (cd "$ROOT/server" && "$MAVEN_CMD" -DskipTests package)
else
  echo "[1/3] 使用现有后端 JAR：$JAR"
fi

if is_port_listening 8080; then
  echo "[2/3] 8080 已在运行，跳过后端启动。"
else
  echo "[2/3] 正在启动 Spring Boot 后端..."
  powershell.exe -NoProfile -Command "Start-Process -FilePath '$JAVA_EXE' -ArgumentList @('-jar','$(cygpath -w "$JAR" 2>/dev/null || printf '%s' "$JAR")') -WorkingDirectory '$WIN_ROOT' -WindowStyle Hidden -RedirectStandardOutput '$(cygpath -w "$SERVER_LOG_DIR/start-out.log" 2>/dev/null || printf '%s' "$SERVER_LOG_DIR/start-out.log")' -RedirectStandardError '$(cygpath -w "$SERVER_LOG_DIR/start-error.log" 2>/dev/null || printf '%s' "$SERVER_LOG_DIR/start-error.log")' -PassThru | Select-Object -ExpandProperty Id" > "$ROOT/.backend.pid"
fi

if is_port_listening 5173; then
  echo "[3/3] 5173 已在运行，跳过前端启动。"
else
  echo "[3/3] 正在启动 Vue 前端..."
  powershell.exe -NoProfile -Command "Start-Process -FilePath '$NPM_CMD' -ArgumentList @('run','dev','--','--host','0.0.0.0','--port','5173') -WorkingDirectory '$WIN_WEB' -WindowStyle Hidden -RedirectStandardOutput '$(cygpath -w "$WEB_LOG_DIR/start-out.log" 2>/dev/null || printf '%s' "$WEB_LOG_DIR/start-out.log")' -RedirectStandardError '$(cygpath -w "$WEB_LOG_DIR/start-error.log" 2>/dev/null || printf '%s' "$WEB_LOG_DIR/start-error.log")' -PassThru | Select-Object -ExpandProperty Id" > "$ROOT/.frontend.pid"
fi

backend_ready=0
for _ in $(seq 1 40); do
  if curl -fsS --max-time 2 http://localhost:8080/api/health >/dev/null 2>&1; then
    backend_ready=1
    break
  fi
  sleep 1
done

frontend_ready=0
for _ in $(seq 1 30); do
  if curl -fsS --max-time 2 http://localhost:5173/login >/dev/null 2>&1; then
    frontend_ready=1
    break
  fi
  sleep 1
done

if [[ "$backend_ready" -ne 1 ]]; then
  echo "后端启动失败，错误日志：$SERVER_LOG_DIR/start-error.log"
  exit 1
fi
if [[ "$frontend_ready" -ne 1 ]]; then
  echo "前端启动失败，错误日志：$WEB_LOG_DIR/start-error.log"
  exit 1
fi

echo ""
echo "项目启动成功"
echo "前端：http://localhost:5173"
echo "后端：http://localhost:8080"
echo "健康检查：http://localhost:8080/api/health"
echo "Swagger：http://localhost:8080/swagger-ui.html"
echo "MySQL57 未由本脚本启动或停止。"