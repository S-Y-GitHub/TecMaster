from time import sleep
import serial
import serial.tools.list_ports

def write(device,bs:bytes):
    comport=serial.Serial(device)
    comport.write(bs)
    comport.flush()
    comport.reset_output_buffer()
    sleep(1)
    comport.close()
    
def read(device,count:int=1):
    comport=serial.Serial(device)
    c=comport.read(count)
    return c