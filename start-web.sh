#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
WEB_DIR="$ROOT/web"
NPM_CMD='D:\DevelopSoftware\VUE\nodejs\npm.cmd'
WIN_WEB="$(cygpath -w "$WEB_DIR" 2>/dev/null || printf '%s' "$WEB_DIR")"
LOG_DIR="$WEB_DIR/logs"
OUT_LOG="$LOG_DIR/frontend-only-out.log"
ERR_LOG="$LOG_DIR/frontend-only-error.log"

mkdir -p "$LOG_DIR"

is_port_listening() {
  netstat -ano 2>/dev/null | grep -E '[:.]5173[[:space:]].*LISTENING' >/dev/null 2>&1
}

if [[ ! -d "$WEB_DIR/node_modules" ]]; then
  echo "未找到 node_modules，正在安装前端依赖..."
  (cd "$WEB_DIR" && "$NPM_CMD" install)
fi

if is_port_listening; then
  echo "Vue 前端已在 5173 运行，无需重复启动。"
else
  echo "正在启动 Vue 前端..."
  powershell.exe -NoProfile -Command "Start-Process -FilePath '$NPM_CMD' -ArgumentList @('run','dev','--','--host','0.0.0.0','--port','5173') -WorkingDirectory '$WIN_WEB' -WindowStyle Hidden -RedirectStandardOutput '$(cygpath -w "$OUT_LOG" 2>/dev/null || printf '%s' "$OUT_LOG")' -RedirectStandardError '$(cygpath -w "$ERR_LOG" 2>/dev/null || printf '%s' "$ERR_LOG")' -PassThru | Select-Object -ExpandProperty Id" > "$ROOT/.frontend-only.pid"
fi

ready=0
for _ in $(seq 1 30); do
  if curl -fsS --max-time 2 http://localhost:5173/login >/dev/null 2>&1; then
    ready=1
    break
  fi
  sleep 1
done

if [[ "$ready" -ne 1 ]]; then
  echo "前端启动失败，请查看：$ERR_LOG"
  exit 1
fi

echo ""
echo "Vue 前端启动成功"
echo "本机访问：http://localhost:5173"
echo "局域网访问：请使用本机 IPv4 地址加端口 5173"
echo "日志文件：$OUT_LOG"
echo "错误日志：$ERR_LOG"
echo "提示：前端依赖后端 http://localhost:8080，当前脚本不会启动后端。"