# -*- coding: utf-8 -*-
"""Comment here

@author wocanmei
@date 2018-07-15 17:56:25
"""

from py4j.java_gateway import JavaGateway
from py4j.java_collections import ListConverter

gateway = None
server = None


def start(port):
    global gateway
    gateway = JavaGateway()
    global server
    server = gateway.jvm.com.pingao.server.MarkDownServer.getInstance()
    server.start(port)


def sync(path, lines, bottom):
    server.broadcast('sync', path, ListConverter().convert(lines, gateway._gateway_client), bottom)


def close(path):
    server.broadcast('close', path, ListConverter().convert([], gateway._gateway_client), 1)


def stop():
    server.destroy()
    gateway.shutdown()
