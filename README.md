# Wechat-Java-Tools
用Java写的微信公众号后端项目


## 项目介绍

这是一个基于Java所实现微信公众号的智能问答和图片的文字识别功能的一个项目。



## 项目效果

![image-20200530161719120](https://tva1.sinaimg.cn/large/007S8ZIlgy1gfajruan4aj30u02sie81.jpg)



## 为什么想做

其实在今年三月份，我做了一个公众号「神搜题」，在阿里云上搭建了一个微信公众号的后端，但后端的代码是通过不断谷歌后的七零八散的PHP代码组装起来的（只是入门了PHP，所以拼凑后的代码乱糟糟）。

在五月上中旬，我有幸参加了阿里云“视觉AI”训练营，但无奈于期中考将近，只粗略做了几个Java程序，并没有将完整的项目呈现。

在期中考结束后，借此机会，用Java重写了公众号的后端配置代码，并将结营前未完成的项目完成。



## 用了什么技术

- 后端：因为还没学习过框架，所以只是用了基础的Servlet实现。（其实在做这个的时候，还是遇到不少的难题，但都通过强大的谷歌解决了，感谢前辈们的贡献！）
- 前端：因为用了公众号，就偷懒啦～（划掉）
- 用到的API：
  1. [阿里云视觉智能开放平台--公众人脸识别](https://vision.aliyun.com/experience/detail?tagName=facebody&children=RecognizePublicFace)
  2. [阿里云视觉智能开放平台--通用识别](https://vision.aliyun.com/experience/detail?spm=a211p3.14020179.J_7524944390.26.66cd4b58m4MyOQ&tagName=ocr&children=RecognizeCharacter)
  3. [阿里云云市场--智能问答](https://market.aliyun.com/products/57126001/cmapi033508.html?spm=5176.2020520132.101.8.40957218HYWwPQ#sku=yuncode2750800001)