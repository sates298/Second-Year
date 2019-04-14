import binascii


ENDING = '01111110'
MAX_FRAME = 64
DATA_BITS = MAX_FRAME - 40


def stuffing(sequence):
    size = len(sequence)
    if size < 5:
        return sequence
    if sequence[size-5:] == '11111':
        sequence = sequence + '0'
    return sequence


def get_changed_file_by_bits_stuffing(file):
    result = ''
    for ch in file:
        result = stuffing(result + ch)
    return result


def crc(sequence):
    final_crc = str(bin(binascii.crc32(bytes(sequence, 'utf-8')))[2:])
    while len(final_crc) < 32:
        final_crc = '0' + final_crc
    return final_crc


def create_frame(sequence):
    return crc(sequence) + sequence + ENDING


def divide_on_sequences(changed_file):
    result = ''
    tmp = ''
    for ch in changed_file:
        tmp += ch
        if len(tmp) == DATA_BITS:
            result += create_frame(tmp)
            tmp = ''
    if len(tmp) > 0:
        result += create_frame(tmp)
    return result


def final_framing(file):
    changed_file = get_changed_file_by_bits_stuffing(file)
    return divide_on_sequences(changed_file)


def decode_stuffing(data):
    result = ''
    tmp = ''
    for ch in data:
        if ch == '0':
            if len(tmp) < 5:
                tmp = ''
                result += ch
            elif tmp == '11111':
                tmp = ''
        else:
            tmp += ch
            result += ch
    return result


def filter_frame(frame):
    if len(frame) <= 40:
        return "length error"
    crc_frame = frame[:32]
    data = frame[32:-8]
    end = frame[-8:]
    if not end == ENDING:
        return "ending error"
    if not crc_frame == crc(data):
        return "crc error"
    return data


def final_decoding(encoded):
    r = int(len(encoded)/MAX_FRAME) + 1
    frames = [encoded[MAX_FRAME*i:MAX_FRAME*(i+1)] for i in range(r)]
    result = ''
    for f in frames:
        result += filter_frame(f)
    return decode_stuffing(result)


