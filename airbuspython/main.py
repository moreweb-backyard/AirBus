import customtkinter as ctk
import socket
import json
import time
from typing import Dict, Any

# =============================================================================
# --- 1. CONFIGURATION & DATABASE ---
# =============================================================================

# IMPORTANT: Change this to the broadcast address of YOUR network.
# If your computer's IP is 192.168.1.x, this is correct.
# If it's 192.168.0.x, change this to '192.168.0.255'.
BROADCAST_IP = '192.168.1.255' 

UDP_PORT = 6000 
SEND_COMMANDS = True # Set to False to run in a test mode that only prints commands

# The entire device database is embedded here.
# TRANSLATION APPLIED: Room names and device statements are translated to English.
DB_JSON = """
{
    "room": [
        {"_id": 1, "room_id": 1, "room_name": "Kitchen"},
        {"_id": 2, "room_id": 2, "room_name": "Living Room"},
        {"_id": 3, "room_id": 3, "room_name": "Master Bedroom"},
        {"_id": 5, "room_id": 5, "room_name": "Webb's Room"}
    ],
    "light": [
        {"_id": 1, "room_id": 1, "subnetID": 1, "deviceID": 47, "light_statement": "Chandelier", "channel": 5},
        {"_id": 2, "room_id": 1, "subnetID": 1, "deviceID": 47, "light_statement": "Halogen", "channel": 6},
        {"_id": 3, "room_id": 1, "subnetID": 1, "deviceID": 47, "light_statement": "Hidden Light", "channel": 7},
        {"_id": 4, "room_id": 5, "subnetID": 1, "deviceID": 47, "light_statement": "Chandelier", "channel": 18},
        {"_id": 5, "room_id": 5, "subnetID": 1, "deviceID": 47, "light_statement": "Halogen", "channel": 19},
        {"_id": 6, "room_id": 5, "subnetID": 1, "deviceID": 47, "light_statement": "Hidden Light", "channel": 20},
        {"_id": 7, "room_id": 3, "subnetID": 1, "deviceID": 47, "light_statement": "Chandelier", "channel": 21},
        {"_id": 8, "room_id": 3, "subnetID": 1, "deviceID": 47, "light_statement": "Halogen", "channel": 22},
        {"_id": 9, "room_id": 3, "subnetID": 1, "deviceID": 47, "light_statement": "Hidden Light", "channel": 23},
        {"_id": 10, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Chandelier 1", "channel": 8},
        {"_id": 11, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Chandelier 1", "channel": 9},
        {"_id": 12, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Chandelier 2", "channel": 10},
        {"_id": 13, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Chandelier 2", "channel": 11},
        {"_id": 14, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Halogen 1", "channel": 12},
        {"_id": 15, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Halogen 2", "channel": 13},
        {"_id": 16, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Hidden 1", "channel": 14},
        {"_id": 17, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Hidden 2", "channel": 15},
        {"_id": 18, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Hallway Halogen", "channel": 16},
        {"_id": 19, "room_id": 2, "subnetID": 1, "deviceID": 47, "light_statement": "Hallway Hidden", "channel": 17}
    ],
    "hvac": [
        {"_id": 1, "room_id": 2, "subnetID": 1, "deviceID": 80, "hvac_statement": "HVAC Unit 1"}
    ],
    "music": [
        {"_id": 2, "room_id": 2, "music_id": 1, "subnetID": 1, "deviceID": 100, "music_remark": "Music Player"}
    ],
    "other": [
        {"_id": 1, "room_id": 2, "subnetID": 1, "deviceID": 47, "other_statement": "All Lights Off Scene", "channel_1": 1, "other_type": 4}
    ],
    "nio": [
        {"_id": 1, "room_id": 2, "subnetID": 1, "deviceID": 13, "nio_remark": "TV Remote", "name1": "vol+", "value1": 112, "name2": "ch+", "value2": 111, "name3": "vol-", "value3": 108, "name4": "ch-", "value4": 109, "name5": "ok", "value5": 116, "name6": "source", "value6": 114, "name8": "mute", "value8": 115, "name14": "on", "value14": 110, "name15": "off", "value15": 110}
    ]
}
"""

# =============================================================================
# --- 2. BACKEND LOGIC (CONTROLLER CLASS) ---
# =============================================================================

# This CRC table is a direct translation from the Java mbufintCRCTable
CRC16_TABLE = [
    0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935,
    4657, 528, 12915, 8786, 21173, 17044, 29431, 25302, 37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334,
    9314, 13379, 1056, 5121, 25830, 29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669,
    13907, 9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390, 55197, 51132,
    18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789, 59790, 63919, 35144, 39273, 43274, 47403,
    23285, 19156, 31415, 27286, 6769, 2640, 14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802,
    27814, 31879, 19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395, 36200, 40265,
    32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374, 57309, 53244, 48923, 44858, 40793, 36728,
    37256, 33193, 45514, 41451, 53516, 49453, 61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679,
    33721, 37784, 41979, 46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270,
    46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798, 25671, 21540, 17413,
    42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939, 14066, 1681, 5808, 26199, 30326, 17941, 22068,
    55628, 51565, 63758, 59695, 39368, 35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403,
    52093, 56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801, 6864, 10931, 14994,
    64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782, 27655, 23652, 19525, 15522, 11395, 7392, 3265,
    61215, 65342, 53085, 57212, 44955, 49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920
]

def get_local_ip():
    """Gets the local IP address of the machine."""
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(('10.255.255.255', 1))
        IP = s.getsockname()[0]
    except Exception:
        IP = '127.0.0.1'
    finally:
        s.close()
    return IP

class SmartBusController:
    """
    Handles all low-level communication and device mapping for the smart home.
    """
    def __init__(self):
        self.db = json.loads(DB_JSON)
        # Room names are now in English
        self.room_names = {room['room_id']: room['room_name'] for room in self.db['room']}
        
        self.lights = self._init_light_controller()
        self.hvacs = self._init_hvac_controller()
        self.ir_remotes = self._init_nio_controller()
        self.scenes = self._init_other_controller()
        self.music_players = self._init_music_controller()

        print("SmartBus Controller Initialized.")

    def _create_device_key(self, room_name, device_name, device_id_num):
        """Creates a standardized, script-friendly key for a device."""
        # Simplified translation map, relying mostly on the translated DB content now.
        # This function now primarily standardizes formatting.
        translation_map = {
            'Kitchen': 'kitchen', 'Living Room': 'living_room', 'Master Bedroom': 'master_bedroom', 'Webb\'s Room': 'child_room',
            'Chandelier': 'chandelier', 'Halogen': 'halogen', 'Hidden Light': 'hidden_light', 'Hallway': 'hallway',
            'Unit': 'unit', 'Player': 'player', 'Scene': 'scene', 'Remote': 'remote'
        }
        
        def standardize(text):
            if not text: return ""
            text = text.replace(' 1', '1').replace(' 2', '2') # Clean up numbering
            for k, v in translation_map.items():
                text = text.replace(k, v)
            return text.lower().replace(' ', '_').replace('__', '_')

        room_name_std = standardize(room_name)
        device_name_std = standardize(device_name)
        
        key = f"{room_name_std}_{device_name_std}_{device_id_num}".lower().replace('__', '_')
        return key

    def _send_udp_command(self, subnet_id: int, device_id: int, operate_code: int, payload: bytes):
        """
        Constructs and sends the UDP packet with the exact structure and CRC
        decoded from the original udp_socket.java file.
        """
        # --- Layer 1: Core Command Packet ---
        payload_len = len(payload)
        core_packet_len = payload_len + 11  # 9 bytes for header + 2 for CRC
        
        core_packet = bytearray(core_packet_len)
        
        core_packet[0] = core_packet_len & 0xFF
        core_packet[1:5] = b'\xbb\xbb\xcc\xcc'  # Magic Bytes
        core_packet[5:7] = operate_code.to_bytes(2, 'big')
        core_packet[7] = subnet_id
        core_packet[8] = device_id
        core_packet[9:9 + payload_len] = payload

        # Calculate CRC for the core packet (excluding the last 2 bytes)
        crc = 0
        for i in range(core_packet_len - 2):
            b = (crc >> 8) & 0xFF
            crc = (crc << 8) ^ CRC16_TABLE[(b ^ core_packet[i]) & 0xFF]
        
        crc &= 0xFFFF
        core_packet[core_packet_len - 2] = (crc >> 8) & 0xFF
        core_packet[core_packet_len - 1] = crc & 0xFF

        # --- Layer 2: "SMARTCLOUD" Wrapper ---
        try:
            local_ip_bytes = socket.inet_aton(get_local_ip())
        except OSError:
            print("ERROR: Could not determine local IP address. Using fallback.")
            local_ip_bytes = b'\x7f\x00\x00\x01'

        smartcloud_header = b'SMARTCLOUD\xaa\xaa'
        
        full_packet = local_ip_bytes + smartcloud_header + core_packet

        print(f"Sending -> Target: {subnet_id}:{device_id} | OpCode: {operate_code} | Payload: {payload.hex(' ')}")
        
        if SEND_COMMANDS:
            try:
                with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
                    sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
                    sock.sendto(full_packet, (BROADCAST_IP, UDP_PORT))
            except Exception as e:
                print(f"ERROR sending UDP packet: {e}")
        else:
            print("--- SENDING DISABLED (test mode) ---")

    def _init_light_controller(self):
        devices = {self._create_device_key(self.room_names.get(l['room_id']), l['light_statement'], l['_id']): l for l in self.db['light']}
        class LightController:
            def __init__(self, devices_map, sender_func): self.devices, self._send = devices_map, sender_func
            def on(self, key: str): self.set_brightness(key, 100)
            def off(self, key: str): self.set_brightness(key, 0)
            def set_brightness(self, key: str, val: int):
                dev = self.devices.get(key)
                if dev: self._send(dev['subnetID'], dev['deviceID'], 0x0031, bytes([dev['channel'], int(val), 0, 0]))
        return LightController(devices, self._send_udp_command)

    def _init_hvac_controller(self):
        devices = {self._create_device_key(self.room_names.get(h['room_id']), h['hvac_statement'], h['_id']): h for h in self.db['hvac']}
        class HvacController:
            def __init__(self, devices_map, sender_func): self.devices, self._send = devices_map, sender_func
            def power_on(self, key: str):
                dev = self.devices.get(key)
                if dev: self._send(dev['subnetID'], dev['deviceID'], 0xE3D8, bytes([1, 1]))
            def power_off(self, key: str):
                dev = self.devices.get(key)
                if dev: self._send(dev['subnetID'], dev['deviceID'], 0xE3D8, bytes([1, 0]))
        return HvacController(devices, self._send_udp_command)

    def _init_nio_controller(self):
        devices = {}
        for nio in self.db['nio']:
            key = self._create_device_key(self.room_names.get(nio['room_id']), nio['nio_remark'], nio['_id'])
            nio['buttons'] = {nio[f'name{i}']: nio[f'value{i}'] for i in range(1, 28) if f'name{i}' in nio and f'value{i}' in nio and nio[f'value{i}'] != 0}
            devices[key] = nio
        class NioController:
            def __init__(self, devices_map, sender_func): self.devices, self._send = devices_map, sender_func
            def send_command(self, key: str, btn_name: str):
                dev = self.devices.get(key)
                if dev and (code := dev['buttons'].get(btn_name)):
                    self._send(dev['subnetID'], dev['deviceID'], 0xE01C, bytes([code, 255]))
        return NioController(devices, self._send_udp_command)

    def _init_other_controller(self):
        devices = {self._create_device_key(self.room_names.get(o['room_id']), o['other_statement'], o['_id']): o for o in self.db.get('other', [])}
        class OtherController:
            def __init__(self, devices_map, sender_func): self.devices, self._send = devices_map, sender_func
            def trigger(self, key: str):
                dev = self.devices.get(key)
                if dev and dev.get('other_type') == 4:
                    self._send(dev['subnetID'], dev['deviceID'], 0xE01C, bytes([dev['channel_1'], 255]))
        return OtherController(devices, self._send_udp_command)
    
    def _init_music_controller(self):
        devices = {self._create_device_key(self.room_names.get(m['room_id']), "music_player", m['_id']): m for m in self.db.get('music', [])}
        class MusicController:
            def __init__(self, devices_map, sender_func): self.devices, self._send = devices_map, sender_func
            def get_status(self, key: str):
                dev = self.devices.get(key)
                if dev: self._send(dev['subnetID'], dev['deviceID'], 0x192E, b'*Z1STATUS?\r')
        return MusicController(devices, self._send_udp_command)

# =============================================================================
# --- 3. GUI COMPONENTS ---
# =============================================================================

class RemoteControlWindow(ctk.CTkToplevel):
    """A pop-up window for an IR remote control."""
    def __init__(self, master, controller, device_key):
        super().__init__(master)
        self.controller = controller
        self.device_key = device_key
        device_info = controller.ir_remotes.devices[device_key]

        self.title(f"Remote: {device_info['nio_remark']}")
        self.geometry("300x400")
        self.resizable(False, False)
        self.transient(master)

        buttons = device_info['buttons']
        
        power_frame = ctk.CTkFrame(self)
        power_frame.pack(pady=10, padx=10, fill='x')
        if 'on' in buttons:
            ctk.CTkButton(power_frame, text="ON", command=lambda: self.controller.send_command(self.device_key, 'on'), fg_color="green").pack(side='left', expand=True, padx=5)
        if 'off' in buttons:
            ctk.CTkButton(power_frame, text="OFF", command=lambda: self.controller.send_command(self.device_key, 'off'), fg_color="red").pack(side='right', expand=True, padx=5)

        grid_frame = ctk.CTkFrame(self)
        grid_frame.pack(pady=10, padx=10, fill='both', expand=True)

        row, col = 0, 0
        # Sort buttons alphabetically for consistent layout
        for name in sorted(buttons.keys()):
            if name in ['on', 'off']: continue
            ctk.CTkButton(grid_frame, text=name, command=lambda n=name: self.controller.send_command(self.device_key, n)).grid(row=row, column=col, padx=5, pady=5, sticky='nsew')
            col = (col + 1) % 3
            if col == 0: row += 1
        
        for i in range(row + 1): grid_frame.grid_rowconfigure(i, weight=1)
        for i in range(3): grid_frame.grid_columnconfigure(i, weight=1)

# =============================================================================
# --- 4. MAIN APPLICATION CLASS ---
# =============================================================================

class App(ctk.CTk):
    def __init__(self):
        super().__init__()
        self.title("Smart Home Control Panel")
        self.geometry("800x600")
        ctk.set_appearance_mode("Dark")
        ctk.set_default_color_theme("blue")

        self.controller = SmartBusController()
        self.remote_windows = {}

        self.grid_rowconfigure(0, weight=1)
        self.grid_columnconfigure(0, weight=1)
        self.tab_view = ctk.CTkTabview(self, anchor="w")
        self.tab_view.grid(row=0, column=0, padx=10, pady=10, sticky="nsew")

        self._create_tabs()

    def _create_tabs(self):
        # Rooms are now labeled in English
        for room in self.controller.db['room']:
            self._populate_tab(self.tab_view.add(room['room_name']), room['room_id'])

    def _populate_tab(self, tab, room_id):
        # Label is now in English
        scroll_frame = ctk.CTkScrollableFrame(tab, label_text=f"{self.controller.room_names[room_id]} Controls")
        scroll_frame.pack(fill="both", expand=True, padx=5, pady=5)
        
        device_types = {
            "Lights": (self.controller.lights.devices, self._create_light_widget, 'light_statement'),
            "HVAC": (self.controller.hvacs.devices, self._create_hvac_widget, 'hvac_statement'),
            "Remotes": (self.controller.ir_remotes.devices, self._create_remote_widget, 'nio_remark'),
            "Scenes": (self.controller.scenes.devices, self._create_scene_widget, 'other_statement')
        }

        for title, (devices, widget_creator, name_key) in device_types.items():
            items_in_room = {k: v for k, v in devices.items() if v['room_id'] == room_id}
            if items_in_room:
                section_frame = ctk.CTkFrame(scroll_frame)
                section_frame.pack(fill='x', padx=10, pady=10)
                ctk.CTkLabel(section_frame, text=title, font=ctk.CTkFont(size=16, weight="bold")).pack(anchor='w', padx=10, pady=(5,10))
                for key, info in items_in_room.items():
                    widget_creator(section_frame, key, info, name_key)

    def _create_light_widget(self, parent, key, info, name_key):
        frame = ctk.CTkFrame(parent, fg_color="transparent")
        frame.pack(fill='x', padx=10, pady=5)
        ctk.CTkLabel(frame, text=info[name_key], width=120, anchor='w').pack(side='left', padx=(0,10))
        
        slider = ctk.CTkSlider(frame, from_=0, to=100, command=lambda v, k=key: self.controller.lights.set_brightness(k, v))
        slider.set(100)
        slider.pack(side='left', fill='x', expand=True)
        
        ctk.CTkButton(frame, text="On", width=50, command=lambda k=key, s=slider: [self.controller.lights.on(k), s.set(100)]).pack(side='right')
        ctk.CTkButton(frame, text="Off", width=50, command=lambda k=key, s=slider: [self.controller.lights.off(k), s.set(0)]).pack(side='right', padx=5)

    def _create_hvac_widget(self, parent, key, info, name_key):
        frame = ctk.CTkFrame(parent, fg_color="transparent")
        frame.pack(fill='x', padx=10, pady=5)
        ctk.CTkLabel(frame, text=info[name_key], width=120, anchor='w').pack(side='left', padx=(0,10))
        ctk.CTkButton(frame, text="Power On", command=lambda k=key: self.controller.hvacs.power_on(k)).pack(side='left', padx=5)
        ctk.CTkButton(frame, text="Power Off", command=lambda k=key: self.controller.hvacs.power_off(k)).pack(side='left')

    def _create_remote_widget(self, parent, key, info, name_key):
        frame = ctk.CTkFrame(parent, fg_color="transparent")
        frame.pack(fill='x', padx=10, pady=5)
        ctk.CTkLabel(frame, text=info[name_key], width=120, anchor='w').pack(side='left', padx=(0,10))
        ctk.CTkButton(frame, text="Open Remote", command=lambda k=key: self.open_remote_window(k)).pack(side='left')
        
    def _create_scene_widget(self, parent, key, info, name_key):
        frame = ctk.CTkFrame(parent, fg_color="transparent")
        frame.pack(fill='x', padx=10, pady=5)
        ctk.CTkLabel(frame, text=info[name_key], width=120, anchor='w').pack(side='left', padx=(0,10))
        ctk.CTkButton(frame, text="Trigger", command=lambda k=key: self.controller.scenes.trigger(k)).pack(side='left')

    def open_remote_window(self, device_key):
        """Creates or focuses a remote control window."""
        if self.remote_windows.get(device_key) and self.remote_windows[device_key].winfo_exists():
            self.remote_windows[device_key].focus()
        else:
            self.remote_windows[device_key] = RemoteControlWindow(self, self.controller, device_key)

# =============================================================================
# --- 5. EXECUTION BLOCK ---
# =============================================================================

if __name__ == "__main__":
    app = App()
    app.mainloop()