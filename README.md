# QDroid

## 简介 (Simple Description)

这是一个 [Amoeba] 子模块, 基于[amoeba_core](https://github.com/Lu7fer/amoeba_core)开发,
通过Websocket连接了go-CQHTTP程序, 以使用Kotlin/Java进行开发.

目前仅支持在IDE中运行, 后续将添加jar扫描功能, 如需打包, 请添加依赖路径到classpath

将amoeba_core打出来的jar放到qdroid/libs文件夹中

## 目录结构 (Directory Structure)

```
--root
  |-amoeba_core
  | |-src
  | |-... 
  |-qdroid
    |-src
    |-...
```

如果没有ChatGPT官方API额度, 可以尝试:
[ChatGPT代理api](https://api2d.com/r/189820)

## 开源协议 (Open Source License)

![GPLv3](https://www.gnu.org/graphics/gplv3-or-later.svg)

[协议详情](https://www.gnu.org/licenses/gpl-3.0.txt)

## 功能 (Function)

- 天气查询
  - 免费API仅支持查询地级市级别天气
  - 天气 地区名
- 自动群签到
- 与ChatGPT聊天
  - 支持多轮对话, 回复相应的消息即可
  - ask 对话内容
  - /ask 对话内容
- 自动加好友
- bot管理员认证
- 撤回消息指令
- 其它功能敬请期待

## 特别鸣谢 (Special Thanks)

[![JetBrains](https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.svg)](https://www.jetbrains.com/idea/)

## 单独运行

运行`distribution`中的 `installDist`, 进入`build/install/qdroid/bin`, 启动启动脚本即可

启动脚本例:

```bash
qdroid.bat cf.vbnm.amoeba.AmoebaKt
```

其中参数是main方法所在类