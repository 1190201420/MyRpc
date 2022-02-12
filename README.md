# MyRpc
A simple Rpc-frame learning from JavaGuide and SoundBrother

Files are in the master branch.

Maybe I will change it to the main branch.

Just wait a long time...

----------------------------------------------------------------------------

好的不用等了，我好像弄不过来，摆了。

说白了就是个简单的Rpc框架，按照声哥的教程写的。

声哥的项目地址：https://github.com/CN-GuoZiyang/My-RPC-Framework

声哥的教程地址：https://blog.csdn.net/qq_40856284/category_10138756.html

敲的过程中遇到了一些问题，给完全没有项目经验的同学分享一下：

### 1、不会使用Maven

以前听说过maven，也跟着某些视频或多或少使用过，但是当自己上手的时候就忘了该怎么用，只是想当一个好使的工具，查资料得到的结果要么是从它的历史开始事无巨细地讲起，要么是牛头不对马嘴，详略反向得当，反正就是没法解决你的问题。

这里给出简单的使用方法：

IDEA新建项目时，选择Java项目下面一栏的Maven项目，剩下的和Java项目一样。

这样IDEA最右边Database下面就会多一个Maven栏，项目里会多一个pom.xml文件。

在文件里新建<dependencies>标签，然后将你需要的包以<dependency>的形式写进去（这里可以百度），发现会冒红，这时候点开右边的Maven栏，点第一个刷新按钮（Reload All Maven Projects），依赖就导入了。

在你做项目遇到没有的类的时候，鼠标移上去more actions，一般会有add maven dependency的选项，点了后会自动帮你导，然后等下面加载栏完成就可以import class了。



### 2、使用@Data注解没有get方法

第一种情况，搜索能解决，安装lombok插件。

第二种情况，你的属性是boolean的，方法不叫getXXX而叫isXXX。（md我找了半天才发现）



### 3、反序列化错误

跟着声哥的教程做到netty序列化那部之后，运行起来提示反序列化错误，然后就开始debug——无从下手。

最后去看github的项目提交，发现两个实体类RpcRequest和RpcResponse比上一次的提交多了一个空构造函数。

加上之后就过了，也可以用@NoArgsConstructor注解。
  
唔，就这么多了，剩下的自己加油咯，最好的解决方法就是直接copy啦。
