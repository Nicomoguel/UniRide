"""
Coordenadas de píxeles – con soporte de origen relativo
--------------------------------------------------------
Muestra la posición del cursor en tiempo real y captura coordenadas
al hacer clic en la pantalla o al presionar F8.

Las coordenadas se expresan en relación a un ORIGEN definido por el
usuario (ideal para obtener coordenadas relativas a un JPanel de Java
Swing). Define el origen haciendo clic en la esquina superior-izquierda
del contenido del panel (no de la barra de título de la ventana).

Factor de escala HiDPI / Retina
────────────────────────────────
En macOS con pantalla Retina, pynput y Tkinter suelen devolver
coordenadas lógicas (÷2). Java Swing moderno (Java 11+) también usa
coordenadas lógicas, así que normalmente 1× basta.
Si los valores siguen siendo el doble de lo esperado, prueba 2×.

Requisitos:
    pip install pynput

En Linux Mint, si tkinter no está instalado:
    sudo apt install python3-tk

En macOS, la primera vez hay que dar permisos en:
    Ajustes del Sistema → Privacidad y Seguridad → Accesibilidad

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
    _r = tk.Tk()
    _r.withdraw()
    mb.showerror(
        "Falta una dependencia",
        "Esta app necesita la librería 'pynput'.\n\n"
        "Instálala con:\n    pip install pynput",
    )
    sys.exit(1)


SYSTEM     = platform.system()
IS_MAC     = SYSTEM == "Darwin"
IS_LINUX   = SYSTEM == "Linux"
IS_WAYLAND = IS_LINUX and os.environ.get("XDG_SESSION_TYPE", "").lower() == "wayland"

# ── Paleta ────────────────────────────────────────────────────────────────────
BG       = "#1e1e2e"
BG_DARK  = "#181825"
BG_PANEL = "#313244"
FG       = "#cdd6f4"
FG_MUTED = "#a6adc8"
FG_DIM   = "#7f849c"
ACCENT   = "#89b4fa"
DANGER   = "#f38ba8"
SUCCESS  = "#a6e3a1"
WARNING  = "#f9e2af"
ORANGE   = "#fab387"


def get_native_fonts():
    return (
        font.nametofont("TkDefaultFont").actual("family"),
        font.nametofont("TkFixedFont").actual("family"),
    )


def _darken(hex_color, factor=0.85):
    c = hex_color.lstrip("#")
    r, g, b = int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16)
    return f"#{int(r*factor):02x}{int(g*factor):02x}{int(b*factor):02x}"


# ── Widgets personalizados ────────────────────────────────────────────────────

class FlatButton(tk.Frame):
    """Botón plano que respeta tema oscuro en macOS."""

    def __init__(self, parent, text, command, color, fg=BG, font_=None):
        super().__init__(parent, bg=color, cursor="hand2",
                         highlightthickness=0, borderwidth=0)
        self._command = command
        self._normal  = color
        self._hover   = _darken(color, 0.85)
        self._lbl = tk.Label(self, text=text, bg=color, fg=fg,
                             font=font_, padx=10, pady=8)
        self._lbl.pack(fill="both", expand=True)
        for w in (self, self._lbl):
            w.bind("<Button-1>", self._on_click)
            w.bind("<Enter>",    self._on_enter)
            w.bind("<Leave>",    self._on_leave)

    def _on_click(self, _):  self._command()
    def _on_enter(self, _):
        self.configure(bg=self._hover);  self._lbl.configure(bg=self._hover)
    def _on_leave(self, _):
        self.configure(bg=self._normal); self._lbl.configure(bg=self._normal)

    def update_style(self, text=None, color=None, fg=None):
        """Actualiza texto y colores en tiempo de ejecución."""
        if color:
            self._normal = color
            self._hover  = _darken(color, 0.85)
            self.configure(bg=color)
            self._lbl.configure(bg=color)
        if fg:    self._lbl.configure(fg=fg)
        if text:  self._lbl.configure(text=text)


class FlatCheck(tk.Frame):
    """Checkbox personalizado compatible con tema oscuro."""

    def __init__(self, parent, text, variable, font_=None, bg=BG, fg=FG):
        super().__init__(parent, bg=bg, cursor="hand2",
                         highlightthickness=0, borderwidth=0)
        self._var  = variable
        self._box  = tk.Label(self, text=self._char(), bg=bg, fg=ACCENT, font=font_)
        self._box.pack(side="left")
        self._text = tk.Label(self, text="  " + text, bg=bg, fg=fg, font=font_)
        self._text.pack(side="left")
        for w in (self, self._box, self._text):
            w.bind("<Button-1>", self._toggle)
        self._var.trace_add("write", lambda *_: self._refresh())

    def _char(self):     return "☑" if self._var.get() else "☐"
    def _toggle(self, _=None): self._var.set(not self._var.get())
    def _refresh(self):  self._box.config(text=self._char())


# ── Aplicación principal ──────────────────────────────────────────────────────

class CoordinatePicker:

    def __init__(self, root):
        self.root = root
        self.root.title("Coordenadas de píxeles")
        self.root.geometry("430x680")
        self.root.minsize(390, 580)
        self.root.configure(bg=BG)

        sans, mono = get_native_fonts()
        self._f_label  = (sans, 9,  "bold")
        self._f_small  = (sans, 9)
        self._f_normal = (sans, 10)
        self._f_button = (sans, 10, "bold")
        self._f_big    = (mono, 22, "bold")
        self._f_mid    = (mono, 13)
        self._f_list   = (mono, 11)

        # Estado
        self.capture_clicks = tk.BooleanVar(value=True)
        self.scale_var      = tk.DoubleVar(value=1.0)
        self.kb_listener    = None
        self.mouse_listener = None
        self.origin_x       = None   # en coordenadas de pantalla (sin escalar)
        self.origin_y       = None
        self.setting_origin = False  # modo "siguiente clic = origen"

        self._build_ui()

        self.root.attributes("-topmost", True)
        self.root.after_idle(lambda: self.root.attributes("-topmost", True))
        if IS_MAC:
            self.root.lift()
            self.root.after(100, self.root.lift)

        self._update_position()
        self._start_listeners()
        self.root.protocol("WM_DELETE_WINDOW", self._on_close)

    # ── Construcción de la interfaz ───────────────────────────────────────────

    def _build_ui(self):

        # ── Cabecera: posición en vivo ────────────────────────────────────
        hdr = tk.Frame(self.root, bg=BG)
        hdr.pack(fill="x", padx=18, pady=(16, 6))

        tk.Label(hdr, text="POSICIÓN DEL CURSOR",
                 font=self._f_label, bg=BG, fg=FG_MUTED).pack(anchor="w")
        self.live_abs = tk.Label(hdr, text="X: 0   Y: 0",
                                 font=self._f_big, bg=BG, fg=ACCENT)
        self.live_abs.pack(pady=(2, 0))
        self.live_rel = tk.Label(hdr, text="Relativo al origen: —",
                                 font=self._f_mid, bg=BG, fg=SUCCESS)
        self.live_rel.pack(pady=(2, 0))

        # ── Panel de origen ───────────────────────────────────────────────
        #
        # INSTRUCCIÓN DE USO:
        # 1. Mueve la ventana Java a donde la quieras.
        # 2. Pulsa "Definir con clic" en este panel.
        # 3. Haz clic en la esquina SUPERIOR-IZQUIERDA del *contenido* del
        #    JPanel (justo debajo de la barra de título de la ventana Java,
        #    en el píxel 0,0 del mapa/imagen).
        # 4. A partir de ahí todas las capturas son relativas a ese punto.
        #
        orig_wrap = tk.Frame(self.root, bg=BG_PANEL)
        orig_wrap.pack(fill="x", padx=18, pady=(4, 4))
        orig = tk.Frame(orig_wrap, bg=BG_PANEL, padx=12, pady=10)
        orig.pack(fill="x")

        tk.Label(orig, text="ORIGEN  —  esquina ↖ del JPanel",
                 font=self._f_label, bg=BG_PANEL, fg=FG_MUTED).pack(anchor="w")

        self.origin_display = tk.Label(orig, text="Sin definir",
                                       font=(self._f_mid[0], 11, "bold"),
                                       bg=BG_PANEL, fg=WARNING)
        self.origin_display.pack(anchor="w", pady=(6, 0))

        btn_row = tk.Frame(orig, bg=BG_PANEL)
        btn_row.pack(fill="x", pady=(8, 0))

        self.btn_origin = FlatButton(
            btn_row, "⌖  Definir con clic",
            self._toggle_origin_mode, color=ACCENT, font_=self._f_small,
        )
        self.btn_origin.pack(side="left", fill="x", expand=True, padx=(0, 4))

        FlatButton(btn_row, "✕ Limpiar",
                   self._clear_origin, color=BG_PANEL, fg=DANGER,
                   font_=self._f_small).pack(side="left")

        # Factor de escala HiDPI / Retina
        scale_row = tk.Frame(orig, bg=BG_PANEL)
        scale_row.pack(fill="x", pady=(10, 0))

        tk.Label(scale_row, text="Factor escala HiDPI:",
                 font=self._f_small, bg=BG_PANEL, fg=FG_DIM).pack(side="left")

        for label, val in [("1×  (normal)", 1.0), ("2×  (Retina)", 2.0)]:
            tk.Radiobutton(
                scale_row, text=label, variable=self.scale_var, value=val,
                bg=BG_PANEL, fg=FG, selectcolor=BG_DARK,
                activebackground=BG_PANEL, activeforeground=FG,
                font=self._f_small,
            ).pack(side="left", padx=(8, 0))

        # ── Toggle de modo de captura ─────────────────────────────────────
        mode = tk.Frame(self.root, bg=BG)
        mode.pack(fill="x", padx=18, pady=(8, 2))

        FlatCheck(mode, "Capturar al hacer clic en pantalla",
                  self.capture_clicks, font_=self._f_normal).pack(anchor="w")
        tk.Label(mode,
                 text="También puedes presionar F8 para capturar sin clic",
                 font=self._f_small, bg=BG, fg=FG_DIM,
                 ).pack(anchor="w", pady=(4, 0))

        # ── Lista de capturas ─────────────────────────────────────────────
        lst_frame = tk.Frame(self.root, bg=BG)
        lst_frame.pack(fill="both", expand=True, padx=18, pady=(8, 4))

        tk.Label(lst_frame, text="COORDENADAS CAPTURADAS (relativas al origen)",
                 font=self._f_label, bg=BG, fg=FG_MUTED).pack(anchor="w")

        lst_box = tk.Frame(lst_frame, bg=BG_PANEL)
        lst_box.pack(fill="both", expand=True, pady=(4, 0))

        sb = tk.Scrollbar(lst_box)
        sb.pack(side="right", fill="y")

        self.listbox = tk.Listbox(
            lst_box, font=self._f_list, bg=BG_PANEL, fg=FG,
            selectbackground=ACCENT, selectforeground=BG,
            yscrollcommand=sb.set, borderwidth=0, highlightthickness=0,
            activestyle="none", exportselection=False,
        )
        self.listbox.pack(fill="both", expand=True, padx=2, pady=2)
        sb.config(command=self.listbox.yview)
        self.listbox.bind("<Double-Button-1>", lambda _: self.copy_selected())

        # ── Botones de acción ─────────────────────────────────────────────
        btn_frame = tk.Frame(self.root, bg=BG)
        btn_frame.pack(fill="x", padx=18, pady=(4, 12))

        FlatButton(btn_frame, "Copiar", self.copy_selected,
                   color=ACCENT, font_=self._f_button,
                   ).pack(side="left", expand=True, fill="x", padx=(0, 4))
        FlatButton(btn_frame, "Borrar todo", self.clear_list,
                   color=DANGER, font_=self._f_button,
                   ).pack(side="left", expand=True, fill="x", padx=(4, 0))

        # ── Barra de estado ───────────────────────────────────────────────
        self.status = tk.Label(
            self.root,
            text="Define el origen (↖ del JPanel) para coordenadas relativas.",
            font=self._f_small, bg=BG_DARK, fg=FG_MUTED,
            anchor="w", padx=12, pady=6,
        )
        self.status.pack(fill="x", side="bottom")

        if IS_WAYLAND:
            self._set_status(
                "⚠ Sesión Wayland: F8 global puede no funcionar. "
                "Usa una sesión X11 si es posible."
            )

    # ── Gestión del origen ────────────────────────────────────────────────────

    def _toggle_origin_mode(self):
        self.setting_origin = not self.setting_origin
        if self.setting_origin:
            self.btn_origin.update_style(text="✕ Cancelar", color=ORANGE, fg=BG)
            self._set_status(
                "⌖ Haz clic en la esquina ↖ del contenido del JPanel…"
            )
        else:
            self.btn_origin.update_style(text="⌖  Definir con clic", color=ACCENT, fg=BG)
            self._set_status("Definición de origen cancelada.")

    def _apply_origin(self, x, y):
        """Fija el punto de referencia y sale del modo de definición."""
        self.origin_x, self.origin_y = x, y
        self.setting_origin = False
        self.btn_origin.update_style(text="⌖  Definir con clic", color=ACCENT, fg=BG)

        sf = self.scale_var.get()
        ox_s, oy_s = int(x * sf), int(y * sf)
        self.origin_display.config(
            text=f"Pantalla ({x}, {y})  →  escalado ({ox_s}, {oy_s})",
            fg=SUCCESS,
        )
        self._set_status(
            f"✓ Origen fijado en pantalla ({x}, {y}). Capturas ahora relativas."
        )

    def _clear_origin(self):
        self.origin_x = self.origin_y = None
        self.origin_display.config(text="Sin definir", fg=WARNING)
        self._set_status("Origen limpiado. Las capturas mostrarán coordenadas absolutas.")

    # ── Conversión de coordenadas ─────────────────────────────────────────────

    def _to_output(self, x, y):
        """
        Convierte coordenadas de pantalla al espacio de salida.

        Pasos:
          1. Aplica el factor de escala HiDPI (si lo hay).
          2. Si hay origen definido, resta su posición escalada.
        """
        sf = self.scale_var.get()
        rx, ry = int(x * sf), int(y * sf)
        if self.origin_x is not None:
            rx -= int(self.origin_x * sf)
            ry -= int(self.origin_y * sf)
        return rx, ry

    # ── Actualización de la posición en vivo ──────────────────────────────────

    def _update_position(self):
        try:
            x = self.root.winfo_pointerx()
            y = self.root.winfo_pointery()
            ox, oy = self._to_output(x, y)
            self.live_abs.config(text=f"X: {ox}   Y: {oy}")
            if self.origin_x is not None:
                self.live_rel.config(text=f"Relativo al origen: ({ox}, {oy})")
            else:
                self.live_rel.config(text="Relativo al origen: —")
        except tk.TclError:
            return
        self.root.after(33, self._update_position)   # ~30 fps

    # ── Listeners de teclado y ratón ──────────────────────────────────────────

    def _start_listeners(self):

        def on_press(key):
            if key == keyboard.Key.f8:
                # Usamos winfo_pointer* para coherencia con el display en vivo
                x = self.root.winfo_pointerx()
                y = self.root.winfo_pointery()
                self.root.after(0, lambda cx=x, cy=y: self._handle_input(cx, cy, "F8"))

        def on_click(x, y, button, pressed):
            # Nota: pynput entrega coordenadas lógicas en macOS (igual que Tk),
            # por lo que son comparables con winfo_pointerx().
            if pressed and button == mouse.Button.left and not self._is_in_window(x, y):
                # Siempre procesar si estamos definiendo origen;
                # si no, respetar la preferencia del usuario.
                if self.setting_origin or self.capture_clicks.get():
                    self.root.after(0, lambda cx=x, cy=y: self._handle_input(cx, cy, "clic"))

        try:
            self.kb_listener = keyboard.Listener(on_press=on_press)
            self.kb_listener.daemon = True
            self.kb_listener.start()

            self.mouse_listener = mouse.Listener(on_click=on_click)
            self.mouse_listener.daemon = True
            self.mouse_listener.start()

        except Exception as exc:
            hint = ""
            if IS_MAC:
                hint = (" Da permiso en Ajustes → Privacidad y Seguridad "
                        "→ Accesibilidad y vuelve a abrir.")
            elif IS_WAYLAND:
                hint = " Wayland no permite escucha global; usa X11."
            self._set_status(f"No se iniciaron los listeners: {exc}.{hint}")

    def _handle_input(self, x, y, source):
        """Router: fija el origen o registra una captura según el modo activo."""
        if self.setting_origin:
            self._apply_origin(x, y)
        else:
            self._add_capture(x, y, source)

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
        rx, ry = self._to_output(x, y)
        entry = f"({rx}, {ry})"
        self.listbox.insert(0, entry)
        self.listbox.selection_clear(0, tk.END)
        self.listbox.selection_set(0)
        self.listbox.see(0)
        self._set_clipboard(entry)
        self._set_status(f"Capturado {entry} con {source} — copiado al portapapeles")

    # ── Utilidades ────────────────────────────────────────────────────────────

    def _set_clipboard(self, text):
        self.root.clipboard_clear()
        self.root.clipboard_append(text)
        # En X11, update_idletasks() fuerza al servidor a registrar la selección.
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

    def _set_status(self, msg):
        self.status.config(text=msg)

    def _on_close(self):
        for lst in (self.kb_listener, self.mouse_listener):
            try:
                if lst is not None:
                    lst.stop()
            except Exception:
                pass
        self.root.destroy()


# ── Punto de entrada ──────────────────────────────────────────────────────────

def main():
    root = tk.Tk()
    CoordinatePicker(root)
    root.mainloop()


if __name__ == "__main__":
    main()
