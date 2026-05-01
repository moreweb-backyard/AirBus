# =============================================================================
# --- PASTE THIS INTO YOUR smart_home_app.py FILE ---
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
        # doesn't even have to be reachable
        s.connect(('10.255.255.255', 1))
        IP = s.getsockname()[0]
    except Exception:
        IP = '127.0.0.1'
    finally:
        s.close()
    return IP

class SmartBusController:
    # ... (keep the __init__ and all the _init_... methods as they are) ...
    # ...
    # ...
    # DELETE the old _send_udp_command and REPLACE it with this new one:
    
    def _send_udp_command(self, subnet_id: int, device_id: int, operate_code: int, payload: bytes):
        """
        Constructs and sends the UDP packet with the exact structure and CRC
        decoded from the original udp_socket.java file.
        """
        # --- Layer 1: Core Command Packet ---
        payload_len = len(payload)
        core_packet_len = payload_len + 11  # 9 bytes for header + 2 for CRC
        
        # Create a mutable bytearray to build the core packet
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
        
        crc &= 0xFFFF # Ensure it's a 16-bit value
        core_packet[core_packet_len - 2] = (crc >> 8) & 0xFF
        core_packet[core_packet_len - 1] = crc & 0xFF

        # --- Layer 2: "SMARTCLOUD" Wrapper ---
        try:
            local_ip_bytes = socket.inet_aton(get_local_ip())
        except OSError:
            print("ERROR: Could not determine local IP address. Using fallback.")
            local_ip_bytes = b'\x7f\x00\x00\x01' # 127.0.0.1

        smartcloud_header = b'SMARTCLOUD\xaa\xaa'
        
        # The final packet sent over UDP
        full_packet = local_ip_bytes + smartcloud_header + core_packet

        print(f"Sending -> Target: {subnet_id}:{device_id} | OpCode: {operate_code} | Payload: {payload.hex(' ')}")
        print(f"Full Packet ({len(full_packet)} bytes): {full_packet.hex(' ').upper()}")

        if SEND_COMMANDS:
            try:
                with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
                    sock.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
                    sock.sendto(full_packet, (BROADCAST_IP, UDP_PORT))
            except Exception as e:
                print(f"ERROR sending UDP packet: {e}")
        else:
            print("--- SENDING DISABLED (test mode) ---")