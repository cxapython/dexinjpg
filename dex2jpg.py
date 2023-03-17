# -*- coding: utf-8 -*-
# @Time : 2023/3/17 7:40 下午
# @Author : chenxiangan
# @File : dex2jpg.py
# @Software: PyCharm

import os

def create():
    # 读取DEX文件
    with open('hidden.dex', 'rb') as f:
        dex_bytes = f.read()

    # 读取JPG文件
    with open('hidden.jpg', 'rb') as f:
        jpg_bytes = f.read()

    # 在JPG文件末尾添加DEX文件内容
    jpg_bytes += dex_bytes

    # 写入新的JPG文件
    with open('new.jpg', 'wb') as f:
        f.write(jpg_bytes)
def read_dex():
    # 读取包含DEX文件的JPG文件
    with open('new.jpg', 'rb') as f:
        jpg_bytes = f.read()

    # 分离JPG文件内容和DEX文件内容
    jpg_size = jpg_bytes.index(b'\xff\xd9') + 2
    jpg_content = jpg_bytes[:jpg_size]
    dex_content = jpg_bytes[jpg_size:]

    # 保存JPG文件和DEX文件
    with open('new2.jpg', 'wb') as f:
        f.write(jpg_content)

    with open('test2.dex', 'wb') as f:
        f.write(dex_content)
# # 读取包含DEX文件的JPG文件
# with open('new.jpg', 'rb') as f:
#     jpg_bytes = f.read()


read_dex()