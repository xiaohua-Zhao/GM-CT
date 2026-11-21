#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

stop_port() {
  local port="$1"
  echo "正在检查端口 $port..."
  powershell.exe -NoProfile -Command "Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique | ForEach-Object { Stop-Process -Id \$_ -Force -ErrorAction SilentlyContinue; Write-Host ('Stopped port $port PID=' + \$_) }" || true
}

stop_port 8080
stop_port 5173
rm -f "$ROOT/.backend.pid" "$ROOT/.frontend.pid"

sleep 1
remaining="$(netstat -ano 2>/dev/null | grep -E '[:.](8080|5173)[[:space:]].*LISTENING' || true)"
if [[ -n "$remaining" ]]; then
  echo "以下端口仍被占用："
  echo "$remaining"
  exit 1
fi

echo "8080 和 5173 已停止。"
echo "MySQL57 保持运行，未被本脚本停止。"