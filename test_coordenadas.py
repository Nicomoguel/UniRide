import sys
import types
import unittest


fake_pynput = types.ModuleType("pynput")
fake_pynput.keyboard = types.SimpleNamespace()
fake_pynput.mouse = types.SimpleNamespace()
sys.modules.setdefault("pynput", fake_pynput)

import coordenadas


class FakeVar:
    def __init__(self, value):
        self.value = value

    def get(self):
        return self.value


class FakeListbox:
    def __init__(self):
        self.items = []
        self.selected = set()
        self.seen_index = None

    def insert(self, index, entry):
        if index == 0:
            self.items.insert(0, entry)
        elif index == coordenadas.tk.END:
            self.items.append(entry)
        else:
            self.items.insert(index, entry)

    def selection_clear(self, start, end):
        self.selected.clear()

    def selection_set(self, index):
        self.selected.add(self._resolve_index(index))

    def see(self, index):
        self.seen_index = self._resolve_index(index)

    def _resolve_index(self, index):
        if index == coordenadas.tk.END:
            return len(self.items) - 1
        return index


class CoordinatePickerTests(unittest.TestCase):
    def test_captures_are_appended_in_capture_order(self):
        picker = coordenadas.CoordinatePicker.__new__(coordenadas.CoordinatePicker)
        picker.scale_var = FakeVar(1.0)
        picker.origin_x = None
        picker.origin_y = None
        picker.listbox = FakeListbox()
        picker._set_clipboard = lambda text: None
        picker._set_status = lambda msg: None

        picker._add_capture(10, 20, "F8")
        picker._add_capture(30, 40, "F8")

        self.assertEqual(picker.listbox.items, ["10 20", "30 40"])
        self.assertEqual(picker.listbox.selected, {1})
        self.assertEqual(picker.listbox.seen_index, 1)


if __name__ == "__main__":
    unittest.main()
