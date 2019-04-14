from crc import final_framing, final_decoding

INPUT_FILE_PATH = "../../res/Input.txt"
OUTPUT_FILE_PATH = "../../res/Output.txt"
DECODED_FILE_PATH = "../../res/Decode.txt"


def read_file(file_path):
    f = open(file_path, "r")
    file_text = ''
    for string in f:
        if string[-1] == '0':
            add = '0'
        elif string[-1] == '1':
            add = '1'
        else:
            add = ''
        file_text += string[:-1] + add
    return file_text


def write_file(out, file_path):
    f = open(file_path, "w")
    final_out = ''
    tmp = ''
    for ch in out:
        tmp += ch
        if len(tmp) == 80:
            final_out += tmp + '\n'
            tmp = ''
    if len(tmp) > 0:
        final_out += tmp
    f.write(final_out)


if __name__ == "__main__":
    FILE = read_file(INPUT_FILE_PATH)
    OUTPUT_FILE = final_framing(FILE)
    DECODED_FILE = final_decoding(OUTPUT_FILE)

    write_file(OUTPUT_FILE, OUTPUT_FILE_PATH)
    write_file(DECODED_FILE, DECODED_FILE_PATH)

    print(FILE == DECODED_FILE)
