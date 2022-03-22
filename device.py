import serial
import serial.tools.list_ports

def getDevice(serial_number):
    VID=1027
    PID=24577
    ports=list(serial.tools.list_ports.comports())
    for port in ports:
        if(port.vid==VID)&(port.pid==PID)&(port.serial_number==serial_number):
            return port.device
    exit(-1)
