#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "正在停止 Vue 前端 5173..."
powershell.exe -NoProfile -Command "Get-NetTCPConnection -LocalPort 5173 -State Listen -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique | ForEach-Object { Stop-Process -Id \$_ -Force -ErrorAction SilentlyContinue; Write-Host ('Stopped Vue PID=' + \$_) }" || true
rm -f "$ROOT/.frontend-only.pid"

sleep 1
if netstat -ano 2>/dev/null | grep -E '[:.]5173[[:space:]].*LISTENING' >/dev/null 2>&1; then
  echo "5173 仍在占用，请检查日志或使用管理员权限运行。"
  exit 1
fi

echo "Vue 前端已停止。"
echo "Spring Boot 8080 和 MySQL57 未被本脚本操作。"