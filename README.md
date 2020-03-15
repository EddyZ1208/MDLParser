# MDLParser

#### 介绍
a parser of the Simulink MDL file

#### 软件架构
dao: 在这里面我放了一个MDLObject类，这是对应于Simulink 的MDL文件结构的类
impl:这里面就是MDLParser类，对mdl文件进行处理，初始化MDLObject对象的地方


#### 安装教程

源代码，无需安装，eclipse打开

#### 使用说明

使用MDLParser 类对MDLObject进行初始化,需要调用一下CenterController方法，在输出完整个MDL文件后，请调用MDLParser中的outputDone方法，否则不会关闭数据流，影响输出完成的模型内容。


