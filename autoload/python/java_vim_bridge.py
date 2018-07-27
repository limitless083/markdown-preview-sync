# -*- coding: utf-8 -*-
"""Bridge connect vim to java

@author wocanmei
@date 2018-07-15 17:56:25
"""

from py4j.java_collections import ListConverter
from py4j.java_gateway import JavaGateway

gateway = None
server = None


def start(port, theme):
    global gateway
    gateway = JavaGateway()
    global server
    server = gateway.jvm.com.pingao.server.MarkDownServer.getInstance()
    server.setTheme(theme)
    server.start(port)


def sync(path, lines, bottom):
    server.broadcast('sync', path, ListConverter().convert(lines, gateway._gateway_client), bottom)


def close(path):
    server.broadcast('close', path, ListConverter().convert([], gateway._gateway_client), 1)


def stop():
    server.destroy()
    gateway.shutdown()
