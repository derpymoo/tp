#!/usr/bin/env bash
set -euo pipefail

# Watches docs/diagrams for PlantUML updates and regenerates PNGs in docs/images.
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DIAGRAM_DIR="$SCRIPT_DIR/diagrams"
IMAGE_DIR="$SCRIPT_DIR/images"
DO_INITIAL_RENDER=false

if [[ "${1:-}" == "--initial" ]]; then
  DO_INITIAL_RENDER=true
fi

mkdir -p "$IMAGE_DIR"

if ! command -v plantuml >/dev/null 2>&1; then
  echo "Error: plantuml is not installed."
  echo "Install it (Ubuntu/Debian): sudo apt-get install -y plantuml graphviz"
  exit 1
fi

render_file() {
  local src="$1"
  local name
  local src_dir
  local relative_out

  name="$(basename "$src")"
  src_dir="$(dirname "$src")"

  if [[ "$name" == "style.puml" ]]; then
    return 0
  fi

  relative_out="$(realpath --relative-to="$src_dir" "$IMAGE_DIR")"
  (
    cd "$src_dir"
    plantuml -tpng -o "$relative_out" "$name"
  )
  echo "Rendered: ${name%.puml}.png"
}

render_all() {
  echo "Rendering all diagrams..."
  while IFS= read -r -d '' file; do
    render_file "$file"
  done < <(find "$DIAGRAM_DIR" -type f -name '*.puml' ! -name 'style.puml' -print0)
  echo "Render pass complete."
}

watch_with_inotify() {
  echo "Watching with inotifywait: $DIAGRAM_DIR"
  inotifywait -m -r -e close_write,moved_to --format '%w%f' "$DIAGRAM_DIR" |
    while IFS= read -r changed; do
      case "$changed" in
        *.puml)
          if [[ ! -f "$changed" ]]; then
            continue
          elif [[ "$(basename "$changed")" == "style.puml" ]]; then
            # Shared style changes can affect all diagrams.
            render_all
          else
            render_file "$changed"
          fi
          ;;
      esac
    done
}

watch_with_polling() {
  echo "inotifywait not found; using polling fallback (2s interval)."
  local previous_snapshot
  previous_snapshot="$(find "$DIAGRAM_DIR" -type f -name '*.puml' -printf '%T@ %p\n' | sort)"

  while true; do
    sleep 2
    local current_snapshot
    current_snapshot="$(find "$DIAGRAM_DIR" -type f -name '*.puml' -printf '%T@ %p\n' | sort)"
    if [[ "$current_snapshot" != "$previous_snapshot" ]]; then
      render_all
      previous_snapshot="$current_snapshot"
    fi
  done
}

if [[ "$DO_INITIAL_RENDER" == true ]]; then
  render_all
else
  echo "Initial full render skipped. Use --initial to regenerate all diagrams first."
fi

if command -v inotifywait >/dev/null 2>&1; then
  watch_with_inotify
else
  watch_with_polling
fi
