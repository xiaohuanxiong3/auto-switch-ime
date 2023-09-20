# self-use-plugins

自用的Jetbrains平台插件

### 翻译插件

#### 2023-07-25

1. 完成翻译设置页面 + 配置持久化  

2. 初步完善翻译api抽象和翻译引擎抽象  

3. 添加默认不翻译引擎和百度翻译引擎

----

#### 2023-07-16

    目前参考 [GitHub - YiiGuxing/TranslationPlugin: Translation plugin for IntelliJ based IDEs/Android Studio.](https://github.com/YiiGuxing/TranslationPlugin) 初步打通了翻译流程

#### TODO（优先级由高到低）

- [ ] 完成 语言适配器 统一不同翻译引擎的语言缩写

- [ ] 进一步抽象翻译api

- [ ] 添加更多翻译引擎支持

### 输入法自动切换插件

    在编写代码时通过识别光标所处位置为注释还是代码自动完成输入法切换

    想法是在某个评论区获取的，看到后感觉很实用。目前正在构思中（就是没时间...）

#### 2023-09-20

    自动切换输入法插件的基本框架搭好了，后续的工作是添加对各种场景的支持，以及最重要的注释区域判断的代码实现
