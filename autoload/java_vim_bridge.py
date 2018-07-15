# -*- coding: utf-8 -*-
"""Comment here

@author wocanmei
@date 2018-07-15 17:56:25
"""

import os
import sys
from py4j.java_gateway import JavaGateway

gateway = JavaGateway()
server = gateway.jvm.com.pingao.server.MarkDownServer()

def start(port):
    server.start(port)

def sync(path, content, bottom):
    server.broadcast('sync', path, content, bottom)

def stop(path):
    server.broadcast('close', path, '', 1)
    server.destroy()
    gateway.shutdown()


if __name__ == '__main__':
    if sys.argv[1] == 'start':
        start(int(sys.argv[2]))
    elif sys.argv[1] == 'sync':
        sync(sys.argv[2], sys.argv[3], int(sys.argv[4]))
    elif sys.argv[1] == 'stop':
        stop(sys.argv[2])
