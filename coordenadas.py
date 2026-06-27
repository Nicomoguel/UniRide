"""
Coordenadas de píxeles - versión multiplataforma
-------------------------------------------------
Muestra la posición del cursor en tiempo real y captura coordenadas
al hacer clic en la pantalla o al presionar F8.

Probado en: Linux Mint (Cinnamon/X11), macOS, Windows.

Requisitos:
    pip install pynput

En Linux Mint, si tkinter no está instalado:
    sudo apt install python3-tk

En macOS, la primera vez hay que dar permisos en:
    Ajustes del Sistema → Privacidad y Seguridad → Accesibilidad
    (autorizar el Terminal o la app desde la que ejecutas Python)

Ejecutar:
    python3 coordenadas.py
"""
import os
import platform
import sys
import tkinter as tk
from tkinter import font

try:
    from pynput import keyboard, mouse
except ImportError:
    import tkinter.messagebox as mb
    _r = tk.Tk(); _r.withdraw()
    mb.showerror(
        "Falta una dependencia",
        "Esta app necesita la librería 'pynput'.\n\n"
        "Instálala con:\n    pip install pynput"
    )
    sys.exit(1)


SYSTEM = platform.system()  # "Linux", "Darwin", "Windows"
IS_MAC = SYSTEM == "Darwin"
IS_LINUX = SYSTEM == "Linux"
IS_WAYLAND = (
    IS_LINUX
    and os.environ.get("XDG_SESSION_TYPE", "").lower() == "wayland"
)


# --- Paleta ---
BG = "#1e1e2e"
BG_DARK = "#181825"
BG_PANEL = "#313244"
FG = "#cdd6f4"
FG_MUTED = "#a6adc8"
FG_DIM = "#7f849c"
ACCENT = "#89b4fa"
DANGER = "#f38ba8"


def get_native_fonts():
    """Devuelve (familia_sans, familia_mono) usando las fuentes nombradas
    de Tk, que siempre resuelven a algo válido en cada OS."""
    return (
        font.nametofont("TkDefaultFont").actual("family"),
        font.nametofont("TkFixedFont").actual("family"),
    )


def _darken(hex_color, factor=0.85):
    c = hex_color.lstrip("#")
    r, g, b = int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16)
    return f"#{int(r * factor):02x}{int(g * factor):02x}{int(b * factor):02x}"


# --- Botón plano: tk.Button no respeta `bg` en macOS,
#     así que usamos un Frame+Label con bindings ---
class FlatButton(tk.Frame):
    def __init__(self, parent, text, command, color, fg=BG, font_=None):
        super().__init__(parent, bg=color, cursor="hand2",
                         highlightthickness=0, borderwidth=0)
        self._command = command
        self._normal = color
        self._hover = _darken(color, 0.85)
        self._label = tk.Label(self, text=text, bg=color, fg=fg,
                               font=font_, padx=10, pady=10)
        self._label.pack(fill="both", expand=True)
        for w in (self, self._label):
            w.bind("<Button-1>", self._on_click)
            w.bind("<Enter>", self._on_enter)
            w.bind("<Leave>", self._on_leave)

    def _on_click(self, _evt):
        self._command()

    def _on_enter(self, _evt):
        self.configure(bg=self._hover)
        self._label.configure(bg=self._hover)

    def _on_leave(self, _evt):
        self.configure(bg=self._normal)
        self._label.configure(bg=self._normal)


# --- Checkbox personalizado: tk.Checkbutton tampoco se ve bien
#     con tema oscuro en macOS ---
class FlatCheck(tk.Frame):
    def __init__(self, parent, text, variable, font_=None, bg=BG, fg=FG):
        super().__init__(parent, bg=bg, cursor="hand2",
                         highlightthickness=0, borderwidth=0)
        self._var = variable
        self._box = tk.Label(self, text=self._box_char(), bg=bg, fg=ACCENT,
                             font=font_)
        self._box.pack(side="left")
        self._text = tk.Label(self, text="  " + text, bg=bg, fg=fg, font=font_)
        self._text.pack(side="left")
        for w in (self, self._box, self._text):
            w.bind("<Button-1>", self._toggle)
        self._var.trace_add("write", lambda *_: self._refresh())

    def _box_char(self):
        return "☑" if self._var.get() else "☐"

    def _toggle(self, _evt=None):
        self._var.set(not self._var.get())

    def _refresh(self):
        self._box.config(text=self._box_char())


class CoordinatePicker:
    def __init__(self, root):
        self.root = root
        self.root.title("Coordenadas de píxeles")
        self.root.geometry("380x540")
        self.root.minsize(340, 440)
        self.root.configure(bg=BG)

        sans, mono = get_native_fonts()
        self._font_label = (sans, 9, "bold")
        self._font_small = (sans, 9)
        self._font_normal = (sans, 10)
        self._font_button = (sans, 10, "bold")
        self._font_big = (mono, 24, "bold")
        self._font_list = (mono, 12)

        self.capture_clicks = tk.BooleanVar(value=True)
        self.kb_listener = None
        self.mouse_listener = None

        # --- Cabecera: posición en vivo ---
        header = tk.Frame(root, bg=BG)
        header.pack(fill="x", padx=18, pady=(18, 8))
        tk.Label(header, text="POSICIÓN DEL CURSOR",
                 font=self._font_label, bg=BG, fg=FG_MUTED).pack(anchor="w")
        self.live_label = tk.Label(header, text="X: 0   Y: 0",
                                   font=self._font_big, bg=BG, fg=ACCENT)
        self.live_label.pack(pady=(4, 0))

        # --- Toggle de modo ---
        mode_frame = tk.Frame(root, bg=BG)
        mode_frame.pack(fill="x", padx=18, pady=4)
        FlatCheck(mode_frame, "Capturar al hacer clic en pantalla",
                  self.capture_clicks, font_=self._font_normal
                  ).pack(anchor="w")
        tk.Label(mode_frame,
                 text="También puedes presionar F8 para capturar sin clic",
                 font=self._font_small, bg=BG, fg=FG_DIM
                 ).pack(anchor="w", pady=(4, 0))

        # --- Lista de capturas ---
        list_frame = tk.Frame(root, bg=BG)
        list_frame.pack(fill="both", expand=True, padx=18, pady=(10, 6))
        tk.Label(list_frame, text="COORDENADAS CAPTURADAS",
                 font=self._font_label, bg=BG, fg=FG_MUTED).pack(anchor="w")

        list_container = tk.Frame(list_frame, bg=BG_PANEL)
        list_container.pack(fill="both", expand=True, pady=(4, 0))

        scrollbar = tk.Scrollbar(list_container)
        scrollbar.pack(side="right", fill="y")
        self.listbox = tk.Listbox(list_container, font=self._font_list,
                                  bg=BG_PANEL, fg=FG,
                                  selectbackground=ACCENT,
                                  selectforeground=BG,
                                  yscrollcommand=scrollbar.set,
                                  borderwidth=0, highlightthickness=0,
                                  activestyle="none",
                                  exportselection=False)
        self.listbox.pack(fill="both", expand=True, padx=2, pady=2)
        scrollbar.config(command=self.listbox.yview)
        self.listbox.bind("<Double-Button-1>", lambda _: self.copy_selected())

        # --- Botones ---
        button_frame = tk.Frame(root, bg=BG)
        button_frame.pack(fill="x", padx=18, pady=(6, 12))
        FlatButton(button_frame, "Copiar", self.copy_selected,
                   color=ACCENT, font_=self._font_button
                   ).pack(side="left", expand=True, fill="x", padx=(0, 4))
        FlatButton(button_frame, "Borrar todo", self.clear_list,
                   color=DANGER, font_=self._font_button
                   ).pack(side="left", expand=True, fill="x", padx=(4, 0))

        # --- Barra de estado ---
        self.status = tk.Label(
            root, text="Listo. Haz clic en la pantalla o presiona F8.",
            font=self._font_small, bg=BG_DARK, fg=FG_MUTED,
            anchor="w", padx=12, pady=6
        )
        self.status.pack(fill="x", side="bottom")

        # Aviso temprano si estamos en Wayland
        if IS_WAYLAND:
            self._set_status(
                "⚠ Sesión Wayland: F8 global puede no funcionar. "
                "Usa una sesión X11 si es posible."
            )

        # Always-on-top con compensación para macOS
        self.root.attributes("-topmost", True)
        self.root.after_idle(lambda: self.root.attributes("-topmost", True))
        if IS_MAC:
            self.root.lift()
            self.root.after(100, self.root.lift)

        self._update_position()
        self._start_listeners()
        self.root.protocol("WM_DELETE_WINDOW", self._on_close)

    def _update_position(self):
        try:
            x = self.root.winfo_pointerx()
            y = self.root.winfo_pointery()
            self.live_label.config(text=f"X: {x}   Y: {y}")
        except tk.TclError:
            return
        self.root.after(33, self._update_position)  # ~30 fps

    def _start_listeners(self):
        def on_press(key):
            if key == keyboard.Key.f8:
                x = self.root.winfo_pointerx()
                y = self.root.winfo_pointery()
                self.root.after(0, lambda: self._add_capture(x, y, "F8"))

        def on_click(x, y, button, pressed):
            if (pressed
                    and button == mouse.Button.left
                    and self.capture_clicks.get()
                    and not self._is_in_window(x, y)):
                self.root.after(0, lambda: self._add_capture(x, y, "clic"))

        try:
            self.kb_listener = keyboard.Listener(on_press=on_press)
            self.kb_listener.daemon = True
            self.kb_listener.start()
            self.mouse_listener = mouse.Listener(on_click=on_click)
            self.mouse_listener.daemon = True
            self.mouse_listener.start()
        except Exception as e:
            hint = ""
            if IS_MAC:
                hint = (" Da permiso en Ajustes → Privacidad y Seguridad "
                        "→ Accesibilidad y vuelve a abrir.")
            elif IS_WAYLAND:
                hint = " Wayland no permite escucha global; usa X11."
            self._set_status(f"No se iniciaron los listeners: {e}.{hint}")

    def _is_in_window(self, x, y):
        try:
            wx = self.root.winfo_rootx()
            wy = self.root.winfo_rooty()
            ww = self.root.winfo_width()
            wh = self.root.winfo_height()
            return wx <= x <= wx + ww and wy <= y <= wy + wh
        except tk.TclError:
            return False

    def _add_capture(self, x, y, source):
        entry = f"({x}, {y})"
        self.listbox.insert(0, entry)
        self.listbox.selection_clear(0, tk.END)
        self.listbox.selection_set(0)
        self.listbox.see(0)
        self._set_clipboard(entry)
        self._set_status(
            f"Capturado {entry} con {source} — copiado al portapapeles"
        )

    def _set_clipboard(self, text):
        self.root.clipboard_clear()
        self.root.clipboard_append(text)
        # En X11 el clipboard solo "existe" mientras alguien lo procesa;
        # update_idletasks() obliga al servidor a registrarlo.
        self.root.update_idletasks()

    def copy_selected(self):
        sel = self.listbox.curselection()
        if not sel:
            if self.listbox.size() > 0:
                self.listbox.selection_set(0)
                sel = (0,)
            else:
                self._set_status("No hay coordenadas para copiar")
                return
        text = self.listbox.get(sel[0])
        self._set_clipboard(text)
        self._set_status(f"Copiado: {text}")

    def clear_list(self):
        self.listbox.delete(0, tk.END)
        self._set_status("Lista vaciada")

    def _set_status(self, message):
        self.status.config(text=message)

    def _on_close(self):
        for lst in (self.kb_listener, self.mouse_listener):
            try:
                if lst is not None:
                    lst.stop()
            except Exception:
                pass
        self.root.destroy()


def main():
    root = tk.Tk()
    CoordinatePicker(root)
    root.mainloop()


if __name__ == "__main__":
    main()
