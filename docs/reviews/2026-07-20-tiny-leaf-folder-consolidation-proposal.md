# 第三轮书签精简方案：合并 1–2 条书签的叶子目录（已执行，待测试反馈）

## 确认与执行结果

已按你确认的规则执行：**原本仅含 1 或 2 条书签的叶子目录，不再单独保留；其书签已根据网站定位和实际使用场景迁移到更稳定的主题目录。**

本轮处理的来源叶子目录已迁空并删除；没有删除书签，也没有修改书签 URL、标题、时间、顶层目录或二级分支。

## 盘点结果

- 当前 AI 叶子目录：**1,980 个**，书签总数：**7,497 条**。
- 待合并叶子目录：**1,246 个**（其中 852 个仅含 1 条书签、394 个含 2 条书签）。
- 待合并书签：**1,640 条**。
- 建议创建/复用的合并目标目录：**81 个**，均位于既有的“顶层 → 二级分支”之下。
- 其余 734 个含 3 条及以上书签的 AI 叶子目录，本轮不动。

## 执行记录

- 执行时间：2026-07-20（Asia/Shanghai）。
- 已迁移书签：**1,640 条**；书签总数仍为 **7,497 条**，根级书签仍为 **0 条**。
- 已删除迁空来源叶子目录：**1,246 个**。
- 已创建主题目标目录：**81 个**（本轮没有可直接复用的现有同名目标目录）。
- 目录总数：从 **2,001 个**减少至 **836 个**，净减少 **1,165 个**。
- 根级目录：仍为 **5 个**。
- 迁移后剩余含 1–2 条书签的叶子目录：**7 个 / 13 条书签**；它们均为本轮刚创建的聚合主题目录，未再做递归合并，以避免把主题目录压平为二级分支。
- 回滚快照：已写入数据库表 `bookmark_tiny_leaf_consolidation_backup`，迁移标识为 `tiny-leaf-consolidation-20260720-v1`，保存了 1,246 个来源目录的原父级、书签数量及目标路径。

## 已执行的整理规则

1. 已将每个 1–2 条书签的来源叶子目录中的书签移动到对应主题目录。
2. 主题目录按书签的 URL、标题、已有 AI `topic_hint`、页面类型与目录逻辑键共同判断，并优先使用使用场景而非机械域名分组。
3. 来源叶子目录迁空后已删除；没有删除书签，也没有修改 URL、标题、时间或顶层/二级分支。
4. 本轮创建了 81 个主题目标目录；后续可继续复用它们作为更细粒度收拢的稳定落点。
5. 成人站点等敏感内容仍只按内容收纳到 `03_创作与娱乐 / 游戏、动漫与休闲 / 成人内容与敏感站点`，不做内容判断或删除。
6. 实际目录净减少 **1,165 个**，与方案预期一致。

## 已执行的各二级分支合并摘要

| 顶层目录 | 二级分支 | 待合并来源目录 | 待迁移书签 | 建议主题目录数 |
| --- | --- | ---: | ---: | ---: |
| `01_技术与AI` | `云服务、运维与网络安全` | 90 | 122 | 8 |
| `01_技术与AI` | `人工智能、模型与自动化` | 36 | 49 | 5 |
| `01_技术与AI` | `数据、硬件与系统工具` | 111 | 150 | 7 |
| `01_技术与AI` | `编程开发与开源` | 96 | 132 | 7 |
| `02_学习与研究` | `论文、科研与学科资料` | 79 | 102 | 5 |
| `02_学习与研究` | `阅读、语言与通识学习` | 53 | 68 | 4 |
| `02_学习与研究` | `高校、留学与考试` | 82 | 112 | 5 |
| `03_创作与娱乐` | `内容创作与社交媒体` | 18 | 26 | 3 |
| `03_创作与娱乐` | `游戏、动漫与休闲` | 88 | 116 | 4 |
| `03_创作与娱乐` | `视频、音频与影视` | 67 | 92 | 4 |
| `03_创作与娱乐` | `设计、图片与视觉素材` | 129 | 170 | 6 |
| `04_工作与生活` | `办公、效率与知识管理` | 37 | 48 | 5 |
| `04_工作与生活` | `商业、营销与电商` | 55 | 69 | 4 |
| `04_工作与生活` | `生活、出行与消费` | 52 | 68 | 5 |
| `04_工作与生活` | `网站、社区与待整理` | 184 | 227 | 5 |
| `04_工作与生活` | `职业、政务与金融` | 69 | 89 | 4 |

## 已执行的主题目录与来源目录汇总

以下按“最终目标目录”汇总。括号内依次为“来源叶子目录数 / 已迁移书签数”。每个来源目录后附其书签标题，便于你抽查网站定位是否合理。

### `01_技术与AI / 云服务、运维与网络安全 / Cloudflare 与边缘服务`（3 个来源目录 / 4 条书签）
- 合并 `Cloudflare Workers资源`（1 条；`small:cloudflare-workers`）→ `Cloudflare 与边缘服务`：Cloudflare Workers优秀开源项目持续收集
- 合并 `Cloudflare资源`（1 条；`small:cloudflare-resources`）→ `Cloudflare 与边缘服务`：Awesome Cloudflare：Cloudflare 服务资源与工具合集
- 合并 `代理与网络运维`（2 条；`small:proxy-operations`）→ `Cloudflare 与边缘服务`：tcp-wss脚本卸载方法与社区讨论帖子；通过 Cloudflare Worker 与 Pages 部署免费 VLESS 节点教程

### `01_技术与AI / 云服务、运维与网络安全 / DevOps、CI/CD 与交付`（7 个来源目录 / 11 条书签）
- 合并 `CI/CD 与 DevOps`（2 条；`small:cicd-and-devops`）→ `DevOps、CI/CD 与交付`：从零搭建持续集成与持续交付CI/CD流水线指南；KubeSphere图形化创建Jenkins流水线配置文档
- 合并 `DevOps 与持续集成`（1 条；`small:devops-cicd`）→ `DevOps、CI/CD 与交付`：使用 Jenkins 自动构建与部署 Vue 项目教程
- 合并 `DevOps与容器技术`（1 条；`small:devops-and-containers`）→ `DevOps、CI/CD 与交付`：Docker Engine容器运行环境安装官方文档
- 合并 `DevOps与开源项目`（2 条；`small:devops-open-source`）→ `DevOps、CI/CD 与交付`：Jpom开源项目运维管理与发布平台；Dromara开源社区项目与技术生态官网
- 合并 `开发运维与持续集成`（1 条；`small:devops-tools`）→ `DevOps、CI/CD 与交付`：192-SIT后端应用持续集成Jenkins任务视图
- 合并 `持续集成与持续交付`（2 条；`small:ci-cd`）→ `DevOps、CI/CD 与交付`：Zadig持续交付平台安装部署官方文档；Jenkins团队接入Zadig实现端到端软件交付教程
- 合并 `部署与DevOps`（2 条；`small:deployment-devops`）→ `DevOps、CI/CD 与交付`：Coolify：开源自托管应用部署与服务器管理平台；Vercel 前端云平台：构建与部署 Web 应用

### `01_技术与AI / 云服务、运维与网络安全 / Linux、NAS 与自建服务`（4 个来源目录 / 5 条书签）
- 合并 `Linux 命令与参考`（1 条；`small:linux-reference`）→ `Linux、NAS 与自建服务`：LinuxCool常用Linux命令手册与在线查询网站
- 合并 `Linux与终端`（1 条；`small:linux-and-terminal`）→ `Linux、NAS 与自建服务`：TTY终端设备概念、历史与Linux交互机制详解
- 合并 `Linux系统镜像与安装`（1 条；`small:linux-installation`）→ `Linux、NAS 与自建服务`：CentOS 8.4.2105 x86_64 安装 ISO 镜像目录
- 合并 `NAS与家庭服务器`（2 条；`small:nas-admin`）→ `Linux、NAS 与自建服务`：局域网Synology DiskStation私有NAS管理后台；局域网Synology DiskStation群晖NAS管理登录界面

### `01_技术与AI / 云服务、运维与网络安全 / 云主机、域名与托管服务`（15 个来源目录 / 22 条书签）
- 合并 `VPS 与云主机`（1 条；`small:vps-cloud-hosting`）→ `云主机、域名与托管服务`：DediPath 全托管 SSD VPS 云服务器产品页
- 合并 `VPS测评与选购`（2 条；`small:vps-resources`）→ `云主机、域名与托管服务`：TopVPS服务器主机测评与VPS选购资讯网站；TopVPS虚拟服务器推荐排行与测评汇总页面
- 合并 `VPS网络优化`（2 条；`small:vps-optimization`）→ `云主机、域名与托管服务`：Linux服务器启用BBR加速进行网络性能优化脚本；低性能VPS线路优化与网络视频传输加速方法
- 合并 `主机服务与账户管理`（1 条；`small:hosting-accounts`）→ `云主机、域名与托管服务`：2345.to服务客户中心与账户管理入口
- 合并 `云平台与部署`（2 条；`small:cloud-platforms`）→ `云主机、域名与托管服务`：Sealos Cloud 云原生应用部署与云资源管理控制台；山海云企业级云计算服务器与VPS服务商
- 合并 `云服务优惠讨论`（1 条；`small:cloud-services-discussions`）→ `云主机、域名与托管服务`：Hostloc论坛讨论谷歌云存储优惠活动
- 合并 `云服务控制台`（1 条；`small:cloud-service-consoles`）→ `云主机、域名与托管服务`：快手云StreamLake开发者服务控制台首页
- 合并 `企业云服务`（1 条；`small:enterprise-cloud-services`）→ `云主机、域名与托管服务`：龙雀云云助理开放门户账号登录入口
- 合并 `域名与主机服务`（1 条；`small:domains-hosting`）→ `云主机、域名与托管服务`：NameSilo 低价域名注册与网站托管服务
- 合并 `开发与自建服务`（2 条；`small:developer-self-hosting`）→ `云主机、域名与托管服务`：Bark iOS 设备自建推送通知服务使用入口；Bark 推送通知服务端的部署配置说明文档
- 合并 `开发云服务`（2 条；`small:developer-cloud-services`）→ `云主机、域名与托管服务`：网易云信即时通讯与音视频通信云服务平台；网易七鱼智能客服开发指南与接口文档概述
- 合并 `服务器与硬件`（1 条；`small:servers-and-hardware`）→ `云主机、域名与托管服务`：选购二手服务器的配置评估与避坑经验分享
- 合并 `网站托管服务`（1 条；`small:web-hosting`）→ `云主机、域名与托管服务`：Serv00 免费托管与服务器服务官网
- 合并 `网络服务购买`（2 条；`small:network-service-purchases`）→ `云主机、域名与托管服务`：Bandwagon Host 搬瓦工 VPS 套餐购物车配置页；Yiyo Networks 会员商店与套餐购买页面
- 合并 `自托管与部署`（2 条；`small:self-hosting`）→ `云主机、域名与托管服务`：Zeabur部署WeWe RSS阅读器指南；懒猫微服Launcher应用启动器与服务管理页面

### `01_技术与AI / 云服务、运维与网络安全 / 云服务与运维综合资源`（25 个来源目录 / 35 条书签）
- 合并 `下载与云存储自动化`（1 条；`small:downloads-and-cloud-storage`）→ `云服务与运维综合资源`：Aria2离线下载并通过Rclone自动上传OneDrive配置
- 合并 `个人云与云桌面`（1 条；`small:personal-cloud`）→ `云服务与运维综合资源`：Puter 浏览器中的开源个人云桌面平台
- 合并 `云原生调试与可观测性`（1 条；`small:cloud-debugging`）→ `云服务与运维综合资源`：Rookout 云原生应用实时动态调试平台
- 合并 `云存储与效率工具`（2 条；`small:cloud-productivity`）→ `云服务与运维综合资源`：Dropbox个人文件管理与云端同步主页；Zoom视频会议账户个人资料设置页面
- 合并 `云存储工具`（1 条；`small:cloud-storage-tools`）→ `云服务与运维综合资源`：NetDrive三跨平台网盘挂载本地磁盘软件
- 合并 `云手机与多账号运营`（2 条；`small:cloud-phone-operations`）→ `云服务与运维综合资源`：比特云手机跨境电商多账号运营平台；比特指纹浏览器云手机环境接口文档
- 合并 `云端应用平台`（1 条；`small:cloud-apps`）→ `云服务与运维综合资源`：UZER.ME云端超级应用与在线软件运行平台
- 合并 `云计算服务`（1 条；`small:cloud-computing`）→ `云服务与运维综合资源`：顺网算力云云端计算资源服务平台
- 合并 `工程仿真与云计算`（1 条；`small:engineering-cloud`）→ `云服务与运维综合资源`：速石科技研发仿真云与高性能计算平台
- 合并 `汽车安全测评`（2 条；`small:auto-safety`）→ `云服务与运维综合资源`：中国保险汽车安全指数车辆碰撞测试结果详情；中国保险汽车安全指数最新动态与测评资讯
- 合并 `系统启动与部署工具`（2 条；`small:boot-and-deployment`）→ `云服务与运维综合资源`：iVentoy局域网多系统镜像PXE启动部署工具；Ventoy Plugson网页端启动盘插件配置工具
- 合并 `网盘与下载工具`（1 条；`small:cloud-downloads`）→ `云服务与运维综合资源`：黑科云破解版自动注册账号与下载链接解析教程
- 合并 `网盘资源与搜索`（1 条；`small:cloud-drive-resources`）→ `云服务与运维综合资源`：找资源阿里云盘公开分享资源搜索引擎
- 合并 `网盘资源搜索`（2 条；`small:cloud-search`）→ `云服务与运维综合资源`：阿里云盘资源搜索与文件检索工具；千帆搜索聚合网盘文件与资源检索工具
- 合并 `网盘资源搜索`（1 条；`small:cloud-storage-resources`）→ `云服务与运维综合资源`：找资源阿里云盘公开资源搜索引擎
- 合并 `网站部署与分析`（2 条；`small:web-deployment-analytics`）→ `云服务与运维综合资源`：Vercel 项目控制台与网站分析数据面板；PinMe：数秒发布静态网站的部署平台
- 合并 `网络与 OpenWrt`（1 条；`small:networking-and-openwrt`）→ `云服务与运维综合资源`：OpenWrt IPoE 宽带认证与动态鉴权配置实战教程
- 合并 `网络与DNS`（2 条；`small:networking-and-dns`）→ `云服务与运维综合资源`：Open vSwitch 虚拟交换机实践配置与原理教程；Sukka分享个人DNS配置方案与使用技巧文章
- 合并 `网络与号码查询工具`（2 条；`small:network-query-tools`）→ `云服务与运维综合资源`：openGPS 高精度IP地址地理位置定位查询；IP138 手机号码归属地与电话号码查询
- 合并 `网络代理与连接服务`（1 条；`small:network-services`）→ `云服务与运维综合资源`：一元机场代理订阅与网络节点管理控制台
- 合并 `网络脚本与规则资源`（1 条；`small:network-scripts`）→ `云服务与运维综合资源`：YBNET 模块插件脚本与去广告规则资源站
- 合并 `网络访问工具`（2 条；`small:network-access`）→ `云服务与运维综合资源`：突破防火长城网络访问工具与方法导航页；Cloudflare 1.1.1.1 加速网络与隐私 DNS 应用
- 合并 `网络诊断工具`（2 条；`small:network-diagnostics`）→ `云服务与运维综合资源`：IPW IPv6 地址归属地与网络信息查询；GFW.Report网络连通性检测报告平台
- 合并 `网络辅助软件`（1 条；`small:network-software`）→ `云服务与运维综合资源`：Watt Toolkit瓦特工具箱与Steam网络辅助软件官网
- 合并 `软件行业与开发安全`（1 条；`small:software-industry`）→ `云服务与运维综合资源`：SonarSource 用户增长与开发安全市场机会分析

### `01_技术与AI / 云服务、运维与网络安全 / 监控、日志与可观测性`（6 个来源目录 / 7 条书签）
- 合并 `云原生与微服务`（2 条；`small:cloud-native-infrastructure`）→ `监控、日志与可观测性`：Higress网关路由规则在线配置演示页面；Nacos 服务发现与配置管理快速入门指南
- 合并 `云服务与基础设施`（1 条；`small:cloud-and-infrastructure`）→ `监控、日志与可观测性`：火山引擎云服务管理控制台首页
- 合并 `基础设施监控`（1 条；`small:infrastructure-monitoring`）→ `监控、日志与可观测性`：Netdata 高分辨率实时基础设施监控平台
- 合并 `天气与灾害监测`（1 条；`small:weather-monitoring`）→ `监控、日志与可观测性`：浙江台风路径实时发布与监测系统
- 合并 `日志与可观测性`（1 条；`small:logging-observability`）→ `监控、日志与可观测性`：lnav 高级日志文件查看与分析工具
- 合并 `网站监控`（1 条；`small:web-monitoring`）→ `监控、日志与可观测性`：changedetection.io 网站页面变化监控与通知告警工具

### `01_技术与AI / 云服务、运维与网络安全 / 网络代理与连通工具`（13 个来源目录 / 20 条书签）
- 合并 `VPN 使用指南`（1 条；`small:vpn-guides`）→ `网络代理与连通工具`：免费VPN选择推荐与使用指南文章
- 合并 `VPN 用户中心`（2 条；`small:vpn-account-centers`）→ `网络代理与连通工具`：我们所向往VPN网络服务用户页面；iKuuu VPN 网络代理服务用户账户首页
- 合并 `代理与网络工具`（1 条；`small:proxy-network-tools`）→ `网络代理与连通工具`：iDaili 免费网页在线代理服务入口
- 合并 `代理工具官方文档`（1 条；`small:proxy-official-docs`）→ `网络代理与连通工具`：V2Ray 模块化代理程序下载安装官方指南
- 合并 `代理工具项目`（1 条；`small:proxy-projects`）→ `网络代理与连通工具`：Shadowsocks 安全 SOCKS5 代理官方项目首页
- 合并 `代理服务导航`（2 条；`small:proxy-navigation`）→ `网络代理与连通工具`：IBCN 网络代理服务导航与站点入口页面；品云机场导航中的超级跑车服务介绍页
- 合并 `代理规则与网络资源`（1 条；`small:proxy-resources`）→ `网络代理与连通工具`：YFamily网络重写模块插件与去广告规则合集
- 合并 `代理软件与项目`（1 条；`small:proxy-software`）→ `网络代理与连通工具`：V2Fly 官方项目网站与 V2Ray 开发信息
- 合并 `开发者网络工具`（2 条；`small:developer-network-tools`）→ `网络代理与连通工具`：GitHub Proxy 镜像加速服务最新地址发布页；CLI Proxy API 的管理中心与接口代理配置
- 合并 `网络与组网工具`（2 条；`small:networking`）→ `网络代理与连通工具`：VNT基于Rust构建的虚拟网络组网工具官网；WireGuard快速现代安全VPN隧道中文介绍网站
- 合并 `网络访问工具指南`（2 条；`small:proxy-service-guides`）→ `网络代理与连通工具`：电脑网络访问工具与VPN加速器选择综合指南；适用于中国地区的免费VPN服务优缺点比较指南
- 合并 `隐私与VPN服务`（2 条；`small:privacy-vpn`）→ `网络代理与连通工具`：AdGuard VPN隐私保护服务中文欢迎与使用入口；AdGuard账户授权认证与我的账户登录页面
- 合并 `隐私与网络工具`（2 条；`small:privacy-and-network-tools`）→ `网络代理与连通工具`：Virtual Browser在线虚拟浏览器服务；快代理企业级HTTP代理IP云服务与网络代理平台

### `01_技术与AI / 云服务、运维与网络安全 / 网络安全、隐私与账号保护`（17 个来源目录 / 18 条书签）
- 合并 `Java认证与权限框架`（1 条；`small:java-security-frameworks`）→ `网络安全、隐私与账号保护`：Sa-Token Java 权限认证与会话管理框架
- 合并 `业务安全与风控`（2 条；`small:security-risk-control`）→ `网络安全、隐私与账号保护`：数美科技在线业务智能风控解决方案官网；数美智能视频文件识别接口开发文档与示例
- 合并 `安全与风控服务`（1 条；`small:security-and-risk-management`）→ `网络安全、隐私与账号保护`：数美科技在线业务风控与反欺诈解决方案官网
- 合并 `安全漏洞与利用`（1 条；`small:security-vulnerabilities`）→ `网络安全、隐私与账号保护`：Exploit Database公开漏洞利用代码与安全漏洞数据库
- 合并 `安全通信应用`（1 条；`small:secure-messaging`）→ `网络安全、隐私与账号保护`：SafeW安全即时通信应用官方网站
- 合并 `安全防护工具`（1 条；`small:security-tools`）→ `网络安全、隐私与账号保护`：火绒安全电脑防护与病毒查杀软件官网
- 合并 `密码与安全管理`（1 条；`small:password-and-security`）→ `网络安全、隐私与账号保护`：1Password 君王账户密码库全部项目管理页面
- 合并 `应用安全`（1 条；`small:application-security`）→ `网络安全、隐私与账号保护`：云鲨 RASP 应用运行时安全威胁防护管理平台
- 合并 `移动通信安全`（1 条；`small:mobile-security`）→ `网络安全、隐私与账号保护`：拦截猫iPhone骚扰电话与垃圾短信防护工具
- 合并 `系统安全工具`（1 条；`small:system-security`）→ `网络安全、隐私与账号保护`：OpenArk 用于系统安全分析的开源反 Rootkit 工具
- 合并 `网站安全检测`（1 条；`small:web-security`）→ `网络安全、隐私与账号保护`：Qualys SSL Labs：在线检测网站 SSL 与 TLS 安全配置
- 合并 `网站检测与安全工具`（1 条；`small:website-security-tools`）→ `网络安全、隐私与账号保护`：Web Check网站安全隐私与技术信息检测工具
- 合并 `网络安全产品`（1 条；`small:cybersecurity-products`）→ `网络安全、隐私与账号保护`：积至网络Cyber Narrator网络叙事分析产品
- 合并 `网络安全资讯与会议`（1 条；`small:cybersecurity-news-and-events`）→ `网络安全、隐私与账号保护`：Hack In The Box全球网络安全会议、新闻与研究资讯
- 合并 `网络隐私与检测`（1 条；`small:network-privacy`）→ `网络安全、隐私与账号保护`：IPCheck：本机公网IP、WebRTC与DNS泄露检测工具
- 合并 `虚拟化安全资讯`（1 条；`small:virtualization-security`）→ `网络安全、隐私与账号保护`：VMware多产品安全漏洞更新公告
- 合并 `身份认证与账户安全`（1 条；`small:identity-security`）→ `网络安全、隐私与账号保护`：Duo Security已验证设备管理门户

### `01_技术与AI / 人工智能、模型与自动化 / AI 写作、办公与文档`（2 个来源目录 / 3 条书签）
- 合并 `AI写作与文本优化`（2 条；`small:ai-writing`）→ `AI 写作、办公与文档`：AiTxt小红书文案与职场文本智能生成助手；ReduceAI 去除文本AI痕迹与润色工具
- 合并 `AI办公与文档生成`（1 条；`small:ai-office`）→ `AI 写作、办公与文档`：讯飞智文 AI在线生成PPT与Word文档

### `01_技术与AI / 人工智能、模型与自动化 / AI 学习、研究与评测`（1 个来源目录 / 2 条书签）
- 合并 `人工智能学习与评测`（2 条；`small:ai-learning`）→ `AI 学习、研究与评测`：C-Eval中文大语言模型多学科能力评测排行榜；伯克利 CS188 2023 秋季人工智能导论课程

### `01_技术与AI / 人工智能、模型与自动化 / AI 对话、助手与搜索`（23 个来源目录 / 31 条书签）
- 合并 `AI 工作流工具`（1 条；`small:ai-workflows`）→ `AI 对话、助手与搜索`：GPT AI Flow 个性化智能助手与工作自动化平台
- 合并 `AI 建站工具`（2 条；`small:ai-website-builders`）→ `AI 对话、助手与搜索`：Wegic 人工智能网站设计与生成应用；Wegic AI 网站设计与开发助手平台
- 合并 `AI 提示词资源`（1 条；`small:ai-prompts`）→ `AI 对话、助手与搜索`：PromptHero生成式AI绘图与对话提示词搜索
- 合并 `AI 搜索与问答`（1 条；`small:ai-search`）→ `AI 对话、助手与搜索`：秘塔AI搜索：面向问答与信息检索的智能搜索引擎
- 合并 `AIGC产业分析`（2 条；`small:aigc-industry-analysis`）→ `AI 对话、助手与搜索`：中国AIGC产业链概念龙头股与市场规模分析文章；AIGC产业爆发背景下科技公司人工智能专利排行榜
- 合并 `AI内容生成`（1 条；`small:ai-content-generation`）→ `AI 对话、助手与搜索`：Pollo AI 多模型人工智能创作平台首页
- 合并 `AI创作与社区`（2 条；`small:ai-creation`）→ `AI 对话、助手与搜索`：YouWare AI创作者Vibe Coding个人主页；YouWare在线AI创作项目编辑器页面
- 合并 `AI工作空间`（2 条；`small:ai-workspaces`）→ `AI 对话、助手与搜索`：Skywork AI Workspace Agents 智能工作空间平台；flowith 2.0 AI 知识驱动创作工作区
- 合并 `AI应用创作平台`（1 条；`small:ai-app-platforms`）→ `AI 对话、助手与搜索`：MyShell AI：创建分享并拥有AI生成应用的平台
- 合并 `AI浏览器`（2 条；`small:ai-browsers`）→ `AI 对话、助手与搜索`：Tabbit AI智能浏览器产品官网；Dia Browser：可与浏览器标签页对话的AI浏览器
- 合并 `AI浏览器与自动化`（1 条；`small:ai-browser-tools`）→ `AI 对话、助手与搜索`：Fellou 深度搜索与自动化智能浏览器
- 合并 `AI硬件与边缘计算`（1 条；`small:ai-hardware`）→ `AI 对话、助手与搜索`：NVIDIA Jetson嵌入式AI开发套件购买页面
- 合并 `AI硬件评估工具`（1 条；`small:ai-hardware-tools`）→ `AI 对话、助手与搜索`：CanIRun.ai本机硬件运行AI模型兼容性检测
- 合并 `AI营销工具`（2 条；`small:ai-marketing-tools`）→ `AI 对话、助手与搜索`：社媒助手用户中心账号与服务管理页面；Outbrand自动化社交媒体内容生成平台
- 合并 `AI行业与技术文章`（1 条；`small:ai-industry-articles`）→ `AI 对话、助手与搜索`：Anthropic Engineering 官方工程技术文章集合
- 合并 `Claude Code文档`（1 条；`small:claude-code-docs`）→ `AI 对话、助手与搜索`：Claude Code中文快速开始与Homebrew安装文档
- 合并 `Claude使用资源`（1 条；`small:claude-resources`）→ `AI 对话、助手与搜索`：Claude指令集中文版本与提示词使用参考
- 合并 `MCP与AI开发工具`（2 条；`small:mcp-and-ai-dev`）→ `AI 对话、助手与搜索`：Glama开源MCP服务器目录与AI工具发现平台；MCP.so模型上下文协议服务器发现与目录平台
- 合并 `人工智能与互联网博客`（1 条；`small:ai-tech-blogs`）→ `AI 对话、助手与搜索`：Jack Cui关注人工智能与互联网技术的个人博客
- 合并 `人工智能产品与公司`（2 条；`small:ai-companies`）→ `AI 对话、助手与搜索`：中科深智人工智能与数字内容技术官网；整数智能人工智能数据与技术服务平台官网
- 合并 `烟台政务经济`（1 条；`small:yantai-government`）→ `AI 对话、助手与搜索`：烟台市发展和改革委员会官方网站与政务信息
- 合并 `生成式人工智能工具`（1 条；`small:generative-ai`）→ `AI 对话、助手与搜索`：Tiamat AI人工智能创意生成平台
- 合并 `语音 AI 工具`（1 条；`small:voice-ai-tools`）→ `AI 对话、助手与搜索`：Voicebox：开源桌面端语音克隆应用

### `01_技术与AI / 人工智能、模型与自动化 / AI 工具导航与平台入口`（4 个来源目录 / 4 条书签）
- 合并 `AI 工具导航`（1 条；`small:ai-tool-directory`）→ `AI 工具导航与平台入口`：Toolify中文AI工具导航与人工智能产品目录
- 合并 `AI 平台账户入口`（1 条；`small:ai-platform-accounts`）→ `AI 工具导航与平台入口`：Fenno 人工智能服务账户注册邀请页面
- 合并 `AI公司与产品官网`（1 条；`small:ai-company-websites`）→ `AI 工具导航与平台入口`：Moonshot AI 月之暗面人工智能产品官网
- 合并 `AI工具评测与导航`（1 条；`small:ai-tools-reviews`）→ `AI 工具导航与平台入口`：Help AIO无赞助AI中转站评测工具横评与教程导航

### `01_技术与AI / 人工智能、模型与自动化 / AI 开发、模型与智能体`（6 个来源目录 / 9 条书签）
- 合并 `AI推理服务`（1 条；`small:ai-inference-services`）→ `AI 开发、模型与智能体`：Inceptron高性价比人工智能推理算力服务平台
- 合并 `AI智能体学习资料`（2 条；`small:ai-agent-learning`）→ `AI 开发、模型与智能体`：Datawhale 通用智能体入门开源教程目录；Datawhale 通用智能体教程第一章环境安装配置
- 合并 `AI智能体开发`（1 条；`small:ai-agent-development`）→ `AI 开发、模型与智能体`：Coze扣子智能体开发平台使用文档中心
- 合并 `AI智能体记忆`（2 条；`small:ai-agent-memory`）→ `AI 开发、模型与智能体`：OpenMem：为智能体提供长期记忆能力的平台；OpenMem 文档：OpenClaw 的 Hermes 本地记忆插件
- 合并 `AI编程学习资料`（1 条；`small:ai-coding-learning`）→ `AI 开发、模型与智能体`：Claude Code 交互式模拟器与入门学习练习
- 合并 `本地AI智能体服务`（2 条；`small:local-ai-agent-services`）→ `AI 开发、模型与智能体`：本地部署 Hermes Agent 会话管理页面；本地 Hermes 智能体记忆服务管理界面

### `01_技术与AI / 数据、硬件与系统工具 / 数据采集、分析与可视化`（14 个来源目录 / 19 条书签）
- 合并 `SEO与网站分析`（1 条；`small:seo-and-analytics`）→ `数据采集、分析与可视化`：Prelaunch.online网站SEO优化建议分析报告
- 合并 `互联网统计资料`（1 条；`small:internet-statistics`）→ `数据采集、分析与可视化`：中国互联网络信息中心官方网络发展统计资料
- 合并 `产品数据分析`（2 条；`small:product-analytics`）→ `数据采集、分析与可视化`：OpenPanel开源产品数据分析与Mixpanel替代方案；Polyscore产品用户分析与智能市场洞察系统
- 合并 `地方经济统计`（1 条；`small:local-economic-statistics`）→ `数据采集、分析与可视化`：2022年青岛市国民经济和社会发展统计公报
- 合并 `媒体资料与数据工具`（1 条；`small:media-data-tools`）→ `数据采集、分析与可视化`：JavHelper 影视演员信息抓取软件介绍与下载页面
- 合并 `数据分析与指标`（1 条；`small:analytics`）→ `数据采集、分析与可视化`：Lecoq嵌入式用户指标数据展示页面
- 合并 `数据分析与统计`（2 条；`small:data-analysis`）→ `数据采集、分析与可视化`：中国统计网数据分析行业资讯与统计技能学习资源；199IT 大数据工具、报告与数据资源导航
- 合并 `数据服务机构`（1 条；`small:data-services`）→ `数据采集、分析与可视化`：人民数据管理有限公司机构介绍与业务信息
- 合并 `数据采集工具`（1 条；`small:data-collection-tools`）→ `数据采集、分析与可视化`：小红书内容采集集成管理平台首页
- 合并 `测试资料生成工具`（1 条；`small:test-data-generators`）→ `数据采集、分析与可视化`：菲律宾地址、身份信息与信用卡信息生成器
- 合并 `算法与数据结构`（2 条；`small:algorithms-and-data-structures`）→ `数据采集、分析与可视化`：柳婼程序猿笔记：算法与软件开发学习博客；labuladong算法小抄：数据结构与算法解题指南
- 合并 `统计分析软件`（1 条；`small:statistics-software`）→ `数据采集、分析与可视化`：华军软件园IBM SPSS Statistics统计软件下载安装页
- 合并 `网站流量与SEO分析`（2 条；`small:website-analytics`）→ `数据采集、分析与可视化`：Similarweb 网站流量与竞争分析平台；爱站网 prpr.xprpr.io 网站综合数据查询
- 合并 `金融数据工具`（2 条；`small:financial-data-tools`）→ `数据采集、分析与可视化`：万得Wind金融终端数据分析与投资研究服务；雪球财经投资研究工具与市场信息导航页面

### `01_技术与AI / 数据、硬件与系统工具 / 文件、下载与格式转换`（12 个来源目录 / 15 条书签）
- 合并 `BT下载资源`（2 条；`small:bt-resources`）→ `文件、下载与格式转换`：RuTracker 俄语资源种子论坛与下载索引；TrackersList 全量BitTorrent Tracker列表文件
- 合并 `下载与云存储服务`（1 条；`small:download-and-storage`）→ `文件、下载与格式转换`：Premiumize 多网盘聚合下载与云端服务平台
- 合并 `下载工具社区`（1 条；`small:download-community`）→ `文件、下载与格式转换`：唧唧社区与女装主题论坛入口
- 合并 `媒体下载工具`（1 条；`small:media-downloaders`）→ `文件、下载与格式转换`：推特X视频图片GIF与音频下载工具
- 合并 `客户端软件下载`（1 条；`small:client-downloads`）→ `文件、下载与格式转换`：macwk-client客户端软件下载与服务入口
- 合并 `数据备份工具`（1 条；`small:data-backup-tools`）→ `文件、下载与格式转换`：MH-Studio微信朋友圈采集与备份工具下载
- 合并 `文件传输与分享`（1 条；`small:file-sharing`）→ `文件、下载与格式转换`：FileCodeBox 文件快递柜临时文件上传分享与取件服务
- 合并 `文件共享与传输`（1 条；`small:file-sharing-tools`）→ `文件、下载与格式转换`：ShareDrop浏览器端局域网点对点文件共享工具
- 合并 `文件同步与备份`（1 条；`small:file-sync-backup`）→ `文件、下载与格式转换`：GoodSync 文件同步、备份与跨设备传输软件
- 合并 `文件管理工具`（1 条；`small:file-utilities`）→ `文件、下载与格式转换`：dupeGuru跨平台重复文件扫描清理工具
- 合并 `磁力工具资讯`（2 条；`small:torrent-guides`）→ `文件、下载与格式转换`：Saber酱博客整理的磁链相关文章标签页；不死鸟BT磁力搜索引擎索引导航
- 合并 `资源下载与分享`（2 条；`small:resource-downloads`）→ `文件、下载与格式转换`：大熊分享站软件课程影视资源聚合首页；大熊分享站综合下载资源分类页面

### `01_技术与AI / 数据、硬件与系统工具 / 浏览器、自动化与效率工具`（11 个来源目录 / 15 条书签）
- 合并 `NAS与私有云远程访问`（2 条；`small:nas-remote-access`）→ `浏览器、自动化与效率工具`：飞牛fnOS私有云FN Connect远程访问入口；飞牛私有云fnOS的FN Connect远程访问服务
- 合并 `OCR与文档智能`（1 条；`small:ocr-and-document-ai`）→ `浏览器、自动化与效率工具`：Tesseract OCR五代接口与开发者API参考文档
- 合并 `OCR文字识别`（1 条；`small:ocr-tools`）→ `浏览器、自动化与效率工具`：在线OCR文字识别与图片文字转换工具
- 合并 `RPA 与流程自动化`（2 条；`small:rpa-automation`）→ `浏览器、自动化与效率工具`：来也 RPA 机器人流程自动化产品文档；UiBot 开发者指南中的 RPA 预备知识
- 合并 `个人自动化`（1 条；`small:personal-automation`）→ `浏览器、自动化与效率工具`：通过 GitHub 快捷指令自动记录每日起床时间的方法
- 合并 `工业自动化`（1 条；`small:industrial-automation`）→ `浏览器、自动化与效率工具`：基恩士中国工业自动化产品与解决方案官网
- 合并 `浏览器与网页技术`（2 条；`small:browsers`）→ `浏览器、自动化与效率工具`：Ladybird：独立开发的开源现代网页浏览器项目；LemurBrowser浏览器新版本测试页面
- 合并 `浏览器使用指南`（1 条；`small:browser-guides`）→ `浏览器、自动化与效率工具`：从 Chrome 浏览器迁移书签和数据到 Safari 教程
- 合并 `浏览器测试工具`（1 条；`small:browser-testing`）→ `浏览器、自动化与效率工具`：Chrome for Testing 浏览器测试版本下载与可用性列表
- 合并 `网页自动化与采集`（1 条；`small:web-automation`）→ `浏览器、自动化与效率工具`：BrowserAct 无代码 AI 网页爬虫与自动化采集工具
- 合并 `远程工作与招聘`（2 条；`small:remote-work`）→ `浏览器、自动化与效率工具`：GitWork：IT研发团队远程工作与程序员兼职平台；Upwork为自由职业者推荐的最佳远程工作机会

### `01_技术与AI / 数据、硬件与系统工具 / 硬件、设备与性能评测`（13 个来源目录 / 17 条书签）
- 合并 `NAS与存储工具`（1 条；`small:nas-and-storage`）→ `硬件、设备与性能评测`：群晖 DS918+ DSM 7.0 系统与实用工具下载中心
- 合并 `外设性能测试`（1 条；`small:peripheral-testing`）→ `硬件、设备与性能评测`：CPS Check 在线鼠标回报率与轮询率检测工具
- 合并 `外设配置工具`（1 条；`small:peripheral-tools`）→ `硬件、设备与性能评测`：罗技Logi Options+设备配置软件官方下载页面
- 合并 `显卡购买与行情`（2 条；`small:gpu-shopping`）→ `硬件、设备与性能评测`：NVIDIA 美国官网 RTX 3070 显卡购买页面；Best Buy 英伟达 RTX 3090 Ti 显卡商品页面
- 合并 `显示器评测与选购`（1 条；`small:display-reviews`）→ `硬件、设备与性能评测`：TFT Central 显示器评测参数资讯与选购指南
- 合并 `显示面板参数查询`（1 条；`small:display-hardware`）→ `硬件、设备与性能评测`：屏库 Panelook 全球液晶显示屏规格参数查询平台
- 合并 `电脑硬件资讯`（2 条；`small:computer-hardware`）→ `硬件、设备与性能评测`：三星980 Pro固态硬盘0E故障与数据备份提醒；TechPowerUp硬件资讯显卡数据库与性能评测平台
- 合并 `硬件与显示测试`（1 条；`small:hardware-testing`）→ `硬件、设备与性能评测`：TestUFO 显示器刷新率与运动模糊在线测试
- 合并 `硬件厂商与产品`（1 条；`small:hardware-companies`）→ `硬件、设备与性能评测`：摩尔线程国产GPU与图形计算产品官方网站
- 合并 `硬件参数与性能对比`（2 条；`small:hardware-comparisons`）→ `硬件、设备与性能评测`：AMD锐龙7 7840HS与锐龙9 7940HS性能规格对比；TopCPU处理器显卡规格性能评级与型号对比
- 合并 `硬件参数与性能对比`（1 条；`small:hardware-reference`）→ `硬件、设备与性能评测`：CPUBoss桌面与移动处理器性能参数对比工具
- 合并 `硬件故障排查`（1 条；`small:hardware-troubleshooting`）→ `硬件、设备与性能评测`：联想社区解决Radeon驱动与设置版本不匹配问题
- 合并 `私有云与NAS管理`（2 条；`small:nas-management`）→ `硬件、设备与性能评测`：homenas 飞牛私有云 fnOS 家庭服务器首页；Immich 私有化照片与相册管理页面

### `01_技术与AI / 数据、硬件与系统工具 / 设备、系统与平台服务`（33 个来源目录 / 46 条书签）
- 合并 `Layui组件与扩展`（1 条；`small:layui-ecosystem`）→ `设备、系统与平台服务`：Layui第三方扩展组件与插件平台
- 合并 `Mac 效率工具`（1 条；`small:mac-utilities`）→ `设备、系统与平台服务`：TabTab 适用于 Mac 的窗口与标签增强管理工具
- 合并 `Mac媒体工具`（1 条；`small:macos-media`）→ `设备、系统与平台服务`：IINA：面向 macOS 的现代开源媒体播放器
- 合并 `Mac应用`（1 条；`small:mac-apps`）→ `设备、系统与平台服务`：Wallspace适用于macOS的动态实时壁纸应用
- 合并 `Mac文件工具`（2 条；`small:macos-file-tools`）→ `设备、系统与平台服务`：MacZip：专为 macOS 设计的文件压缩与解压软件；QSpace：支持多面板操作的 macOS Finder 替代品
- 合并 `Mac虚拟机工具`（2 条；`small:macos-virtualization`）→ `设备、系统与平台服务`：Parallels Desktop 19 启动器破解版下载与使用说明；Parallels Desktop 19 永久授权破解激活教程
- 合并 `Windows优化工具`（1 条；`small:windows-tools`）→ `设备、系统与平台服务`：BoosterX：面向游戏玩家的 Windows 系统优化工具介绍
- 合并 `Windows故障排除`（2 条；`small:windows-troubleshooting`）→ `设备、系统与平台服务`：Windows账户登录错误0x80190001解决教程；Windows 10中Microsoft Edge无法打开解决方法
- 合并 `Windows美化工具`（1 条；`small:windows-customization`）→ `设备、系统与平台服务`：TranslucentTB任务栏透明化工具下载页面
- 合并 `Windows软件管理`（1 条；`small:windows-software-management`）→ `设备、系统与平台服务`：Chocolatey Windows 命令行软件包管理器官方网站
- 合并 `iOS应用发现`（1 条；`small:ios-app-discovery`）→ `设备、系统与平台服务`：Departures平台探索与分享TestFlight测试应用
- 合并 `iOS应用管理`（1 条；`small:ios-app-management`）→ `设备、系统与平台服务`：爱思助手IPA应用签名操作图文教程
- 合并 `iOS越狱资源`（1 条；`small:ios-jailbreak`）→ `设备、系统与平台服务`：Jailbreaks.app 苹果设备越狱工具与资源导航
- 合并 `macOS 与黑苹果`（2 条；`small:macos-hackintosh`）→ `设备、系统与平台服务`：黑果小兵笔记本 Hackintosh 长期维护机型清单；macOS 应用提示已损坏无法打开的修复方法
- 合并 `macOS 效率工具`（1 条；`small:macos-utilities`）→ `设备、系统与平台服务`：DockX桌面程序坞与菜单栏管理工具
- 合并 `一加资源`（2 条；`small:oneplus-resources`）→ `设备、系统与平台服务`：一加 OnePlus 品牌相关网址导航与资源入口；一加社区关于 OnePlus 12 使用谷歌应用的讨论帖
- 合并 `内部企业管理系统`（2 条；`small:internal-business-systems`）→ `设备、系统与平台服务`：至恒融兴EMP企业管理平台首页入口；至恒融兴企业管理平台登录会话页面
- 合并 `创意作品集`（2 条；`small:creative-portfolios`）→ `设备、系统与平台服务`：Cervantes Wu 自由艺术家个人作品集；dearpluto 创意项目个人网站首页
- 合并 `创意工作室作品`（2 条；`small:creative-studios`）→ `设备、系统与平台服务`：THE LINE伦敦动画工作室官方作品展示；ILA创意工作室品牌与视觉作品集网站
- 合并 `多边外交资料`（1 条；`small:multilateral-diplomacy`）→ `设备、系统与平台服务`：外交部中国与国际组织及会议关系资料页面
- 合并 `安卓客户端资源`（1 条；`small:android-client-downloads`）→ `设备、系统与平台服务`：机场网盘安卓客户端资源分享目录
- 合并 `安卓玩机社区`（2 条；`small:android-communities`）→ `设备、系统与平台服务`：MIUI 官方论坛与小米手机系统交流社区；XDA Developers 华为 Mate 30 Pro 玩机开发论坛
- 合并 `宏观经济与市场数据`（1 条；`small:macro-and-market-data`）→ `设备、系统与平台服务`：Trading Economics全球宏观经济指标与市场数据平台
- 合并 `宏观经济与投资数据`（1 条；`small:macro-data`）→ `设备、系统与平台服务`：中投大数据宏观经济与投资数据查询平台
- 合并 `并发与系统原理`（1 条；`small:concurrency-and-systems`）→ `设备、系统与平台服务`：Russ Cox关于并发程序内存模型与内存访问规则的文章
- 合并 `操作系统下载`（1 条；`small:operating-systems`）→ `设备、系统与平台服务`：Ubuntu服务器版ARM架构系统官方下载页面
- 合并 `硬件与驱动下载`（1 条；`small:hardware-drivers`）→ `设备、系统与平台服务`：NVIDIA GeForce显卡驱动程序官方下载入口
- 合并 `系统安装与启动工具`（1 条；`small:system-installation`）→ `设备、系统与平台服务`：Ventoy开源多系统启动U盘制作工具中文官网
- 合并 `系统维护与数据恢复`（2 条；`small:system-maintenance`）→ `设备、系统与平台服务`：易我科技数据恢复、备份与磁盘分区管理工具官网；EaseUS Data Recovery Wizard数据恢复软件资源页面
- 合并 `系统维护与磁盘工具`（2 条；`small:system-utilities`）→ `设备、系统与平台服务`：DiskGenius磁盘分区与数据恢复软件官方下载；易我磁盘拷贝硬盘克隆与系统迁移软件
- 合并 `苹果生态资讯与资源`（1 条；`small:apple-ecosystem`）→ `设备、系统与平台服务`：玩转苹果苹果产品软件资讯与资源平台
- 合并 `苹果设备与macOS`（2 条；`small:macos-apple`）→ `设备、系统与平台服务`：订购M1 Pro十六英寸MacBook Pro后的使用思考；MediaFire 下载 macOS Sonoma 系统 ISO 镜像
- 合并 `驱动与硬件支持`（2 条；`small:drivers-and-hardware`）→ `设备、系统与平台服务`：AMD Radeon RX五八零显卡驱动与支持页面；雷柏V21S鼠标驱动程序下载中心

### `01_技术与AI / 数据、硬件与系统工具 / 通信、网络与数字身份`（2 个来源目录 / 2 条书签）
- 合并 `Telegram 社群与数据`（1 条；`small:telegram-communities`）→ `通信、网络与数字身份`：TGStat影视分享Telegram群组数据统计页面
- 合并 `eSIM通信服务`（1 条；`small:esim-services`）→ `通信、网络与数字身份`：EasyEUICC应用下载与eUICC购买服务平台

### `01_技术与AI / 数据、硬件与系统工具 / 通用软件与系统资源`（26 个来源目录 / 36 条书签）
- 合并 `MBTI 人格分析`（2 条；`small:mbti-personality`）→ `通用软件与系统资源`：16Personalities总经理ESTJ人格特点与分析；16Personalities调停者INFP人格特点与分析
- 合并 `RAG与知识库系统`（2 条；`small:rag-and-knowledge-base`）→ `通用软件与系统资源`：FIT2CLOUD开源社区MaxKB产品入门指南；MaxKB开源大模型RAG知识库问答系统官网
- 合并 `主机系统固件`（1 条；`small:console-firmware`）→ `通用软件与系统资源`：PlayStation官方PS3系统软件固件更新支持页
- 合并 `人力资源管理系统`（2 条；`small:hr-management`）→ `通用软件与系统资源`：北森iTalent一体化人才管理云平台登录入口；聘聘云一体化智能人力资源管理系统官网
- 合并 `健康与公共数据`（1 条；`small:health-data`）→ `通用软件与系统资源`：约翰霍普金斯大学COVID-19全球疫情地图
- 合并 `公共数据查询`（2 条；`small:public-data`）→ `通用软件与系统资源`：国家药品监督相关数据库查询页面；中国药品电子监管网官方网站首页
- 合并 `加密货币市场数据`（1 条；`small:cryptocurrency-data`）→ `通用软件与系统资源`：币安ADA季度合约每日盘口深度历史数据目录
- 合并 `商业与创业数据`（1 条；`small:business-data`）→ `通用软件与系统资源`：IT桔子新经济行业倒闭公司数据库查询
- 合并 `城市生活成本数据`（1 条；`small:city-cost-data`）→ `通用软件与系统资源`：Numbeo北京生活成本、物价与消费水平统计
- 合并 `天气与气象数据`（1 条；`small:weather-data`）→ `通用软件与系统资源`：上海二零二二年四月历史天气查询
- 合并 `微信数据工具`（2 条；`small:wechat-tools`）→ `通用软件与系统资源`：WxDatViewer微信聊天图片查看导出工具；楼月微信聊天记录恢复工具下载页面
- 合并 `手机购物`（2 条；`small:mobile-shopping`）→ `通用软件与系统资源`：丰泽商城苹果iPhone 17 Pro Max商品页；csl香港网店苹果iPhone产品专区
- 合并 `手机通信套餐`（1 条；`small:mobile-plans`）→ `通用软件与系统资源`：T-Mobile三美元预付费月租套餐价值分析
- 合并 `政府开放数据`（1 条；`small:government-open-data`）→ `通用软件与系统资源`：烟台市公共数据开放平台数据资源查询入口
- 合并 `新媒体与内容数据`（2 条；`small:content-platform-data`）→ `通用软件与系统资源`：新榜新媒体账号排行榜与内容传播数据分析；VidStatsX在线视频与YouTube频道数据统计平台
- 合并 `电脑硬件与系统维护`（1 条；`small:pc-maintenance`）→ `通用软件与系统资源`：图吧工具箱纯净电脑硬件检测维护工具集
- 合并 `电脑装机指南`（1 条；`small:pc-building`）→ `通用软件与系统资源`：英特尔游戏电脑装机配置与组装教程
- 合并 `电脑软件工具`（1 条；`small:software-utilities`）→ `通用软件与系统资源`：Patch My PC Home Updater 家用电脑软件自动更新工具
- 合并 `自托管存储与文件管理`（1 条；`small:self-hosted-storage`）→ `通用软件与系统资源`：AList 多存储挂载与文件列表程序中文使用文档
- 合并 `自由职业与远程工作`（1 条；`small:freelancing`）→ `通用软件与系统资源`：Upwork自由职业项目沟通消息房间
- 合并 `虚拟化与系统资源`（2 条；`small:virtualization-resources`）→ `通用软件与系统资源`：Windows 10 中 Hyper-V 虚拟机无法联网的排障教程；Sysin System Inside虚拟化与系统资源汇总
- 合并 `设备固件与系统`（1 条；`small:device-firmware`）→ `通用软件与系统资源`：一加手机系统固件下载与软件更新支持页面
- 合并 `资源下载论坛`（1 条；`small:resource-forums`）→ `通用软件与系统资源`：RuTracker 俄语资源种子论坛首页
- 合并 `隐私与匿名工具`（1 条；`small:privacy-tools`）→ `通用软件与系统资源`：Tor Project浏览器下载完成确认页面
- 合并 `隐私与去中心化网络`（2 条；`small:privacy-and-decentralization`）→ `通用软件与系统资源`：PrivacysClub 隐私保护工具与网络隐私信息汇总站点；Freenet去中心化匿名网络与抗审查项目官网
- 合并 `隐私与搜索工具`（2 条；`small:privacy-and-search`）→ `通用软件与系统资源`：夸克浏览器官方网站与智能搜索服务入口；DuckDuckGo 隐私浏览器扩展与移动应用介绍页

### `01_技术与AI / 编程开发与开源 / 前端与 Web 开发`（17 个来源目录 / 26 条书签）
- 合并 `CSS布局参考`（1 条；`small:css-layout-reference`）→ `前端与 Web 开发`：CSS Layout常用网页布局代码示例集合
- 合并 `CSS预处理器`（1 条；`small:css-preprocessors`）→ `前端与 Web 开发`：Sass中文网CSS预处理器语言学习文档
- 合并 `JavaScript 与 Node.js`（2 条；`small:javascript-node`）→ `前端与 Web 开发`：npm官方JavaScript包管理与依赖检索平台；Node.js中文官方网站与JavaScript运行时资源
- 合并 `JavaScript与Node开发`（1 条；`small:javascript-development`）→ `前端与 Web 开发`：Node.js JavaScript 运行时官方网站与版本下载
- 合并 `JavaScript基础库`（2 条；`small:javascript-libraries`）→ `前端与 Web 开发`：jQuery官方库下载与版本发布页面；Day.js 中文网日期时间处理 JavaScript 库文档
- 合并 `JavaScript生态`（2 条；`small:javascript-ecosystem`）→ `前端与 Web 开发`：Yarn JavaScript 包管理器官方网站与使用资源；Deno新一代JavaScript与TypeScript运行时官网
- 合并 `React与前端开发`（1 条；`small:react-and-frontend`）→ `前端与 Web 开发`：React技术揭秘：从源码理解协调、渲染与提交流程
- 合并 `React开发`（1 条；`small:react`）→ `前端与 Web 开发`：React 官方中文文档与用户界面开发学习指南
- 合并 `Web 开发文档`（2 条；`small:web-documentation`）→ `前端与 Web 开发`：MDN CSS border 边框属性中文参考文档；MDN Web 开发技术中文文档导航
- 合并 `前端与Web开发`（2 条；`small:web-development`）→ `前端与 Web 开发`：Hexo快速静态博客框架中文官方网站与文档；Vite 官方中文文档开始指南与前端构建入门
- 合并 `前端工程化`（1 条；`small:frontend-engineering`）→ `前端与 Web 开发`：当前生产环境部署ES2015及更高版本JavaScript代码
- 合并 `前端开发规范`（2 条；`small:frontend-standards`）→ `前端与 Web 开发`：阿里巴巴前端开发规范英文版文档；阿里巴巴前端开发规范中文版本文档
- 合并 `前端技术博客`（2 条；`small:frontend-blogs`）→ `前端与 Web 开发`：OBKoro1前端进阶积累网站作者与内容介绍页面；海岛心hey前端工程师个人技术博客主页
- 合并 `前端构建工具`（2 条；`small:frontend-build-tools`）→ `前端与 Web 开发`：webpack中文文档中的sass-loader配置指南；Vite快速开始指南与社区项目模板入口
- 合并 `前端架构与微前端`（1 条；`small:frontend-architecture`）→ `前端与 Web 开发`：MicroApp 京东开源微前端应用框架文档
- 合并 `前端组件与资源`（2 条；`small:frontend-resources`）→ `前端与 Web 开发`：Uiverse 免费 CSS 与 Tailwind UI 组件资源库；Tailwind Awesome 免费 Tailwind UI 套件资源筛选页
- 合并 `前端组件文档`（1 条；`small:frontend-docs`）→ `前端与 Web 开发`：EmbedPDF 可嵌入 PDF 阅读器组件开发文档

### `01_技术与AI / 编程开发与开源 / 后端、语言与服务端开发`（9 个来源目录 / 13 条书签）
- 合并 `Java中间件`（2 条；`small:java-middleware`）→ `后端、语言与服务端开发`：JBoss Application Server应用服务器官方下载页面；JBoss与WildFly目录部署失败问题论坛讨论
- 合并 `Java数据访问框架`（2 条；`small:java-data-access`）→ `后端、语言与服务端开发`：MyBatis-Plus Java 数据库持久层增强框架；Brucege 博客中的 MyBatis 插件付费页面
- 合并 `Node.js开发`（1 条；`small:nodejs-development`）→ `后端、语言与服务端开发`：dotenv环境变量配置Node.js软件包说明
- 合并 `Python 工具与发行版`（1 条；`small:python-tools`）→ `后端、语言与服务端开发`：Anaconda Python 发行版历史版本安装包下载目录
- 合并 `Python开发环境`（1 条；`small:python-environments`）→ `后端、语言与服务端开发`：Anaconda个人版Python数据科学环境官方下载页面
- 合并 `Spring Boot开发`（1 条；`small:spring-boot`）→ `后端、语言与服务端开发`：Spring Boot 与 IDEA 实现代码热部署教程
- 合并 `后端开发平台`（2 条；`small:backend-platforms`）→ `后端、语言与服务端开发`：Supabase：开源 Firebase 替代方案与后端服务平台；Supabase demo 项目后台控制台与数据库管理
- 合并 `后端开源项目`（1 条；`small:backend-projects`）→ `后端、语言与服务端开发`：FEBS开源后台用户与权限管理系统演示
- 合并 `数据库开发`（2 条；`small:database-development`）→ `后端、语言与服务端开发`：ioredis Node.js Redis 客户端完整 API 文档；ClickHouse Playground 在线 SQL 查询与分析演示环境

### `01_技术与AI / 编程开发与开源 / 建站、CMS 与技术写作`（2 个来源目录 / 2 条书签）
- 合并 `Notion 建站工具`（1 条；`small:notion-publishing`）→ `建站、CMS 与技术写作`：NotionNext 将 Notion 笔记快速发布为网站的建站工具
- 合并 `内容管理与建站`（1 条；`small:content-management`）→ `建站、CMS 与技术写作`：WordPress 官方开源博客发布平台与内容管理系统

### `01_技术与AI / 编程开发与开源 / 开发学习与职业成长`（2 个来源目录 / 4 条书签）
- 合并 `开发者学习路线`（2 条；`small:developer-learning`）→ `开发学习与职业成长`：roadmap.sh开发者学习路线图平台；计算机教育中缺失的一课命令行开发工具中文教程
- 合并 `数据库学习`（2 条；`small:database-learning`）→ `开发学习与职业成长`：W3School 中文 SQL 数据库查询教程；DB-TUTORIAL数据库基础、SQL与系统原理学习教程

### `01_技术与AI / 编程开发与开源 / 开发工具、环境与工程实践`（37 个来源目录 / 46 条书签）
- 合并 `Android开发`（1 条；`small:android-development`）→ `开发工具、环境与工程实践`：Android SDK Platform Tools 平台工具版本说明
- 合并 `Qt 开发框架`（1 条；`small:qt-development`）→ `开发工具、环境与工程实践`：Qt 中文官网：跨平台嵌入式与桌面应用开发框架
- 合并 `二维码工具`（1 条；`small:qr-code-tools`）→ `开发工具、环境与工程实践`：草料二维码图片转二维码在线生成工具
- 合并 `代码生成工具`（2 条；`small:code-generators`）→ `开发工具、环境与工程实践`：CodeFun 将 UI 设计稿智能转换为前端源代码；Easy Code 面向业务系统的在线代码生成器
- 合并 `低代码开发平台`（1 条；`small:low-code`）→ `开发工具、环境与工程实践`：美乐低代码企业数字化应用开发平台
- 合并 `低代码开发平台`（2 条；`small:low-code-development`）→ `开发工具、环境与工程实践`：FormCreate开源低代码表单设计器在线演示；JEECG低代码开发平台官方演示账号页面
- 合并 `区块链开发社区`（1 条；`small:blockchain-development`）→ `开发工具、环境与工程实践`：登链社区区块链开发技术交流与学习平台
- 合并 `在线开发工具`（1 条；`small:online-developer-tools`）→ `开发工具、环境与工程实践`：在线UUID生成器用于创建唯一标识符
- 合并 `小程序开发`（1 条；`small:mini-program-development`）→ `开发工具、环境与工程实践`：Taro 小程序主包优化编译配置参数详细说明
- 合并 `开发协作平台`（2 条；`small:developer-collaboration`）→ `开发工具、环境与工程实践`：junw555的JetBrains Space团队协作空间主页；RabbitMQ 官方 Git 与 GitHub 协作贡献指南
- 合并 `开发平台与 IDE`（1 条；`small:developer-platforms`）→ `开发工具、环境与工程实践`：OpenSumi开源IDE框架与可扩展开发平台中文站
- 合并 `开发平台账户管理`（1 条；`small:developer-platform-accounts`）→ `开发工具、环境与工程实践`：Vercel 账户设置与个人账号管理页面
- 合并 `开发文档与知识库`（1 条；`small:developer-docs`）→ `开发工具、环境与工程实践`：Hao-Docs 技术知识库与开发文档站点
- 合并 `开发环境与包管理`（1 条；`small:package-managers`）→ `开发工具、环境与工程实践`：Homebrew macOS与Linux软件包管理器官网
- 合并 `开发环境配置`（2 条；`small:development-environments`）→ `开发工具、环境与工程实践`：Omakub：DHH 为 Ubuntu 24.04 提供的开发环境配置；vfox 多语言开发环境版本管理器快速入门
- 合并 `开发者主页与作品集`（1 条；`small:developer-portfolios`）→ `开发工具、环境与工程实践`：Joohnsmith 个人网站或开发者作品主页
- 合并 `开发者周边商品`（1 条；`small:developer-merchandise`）→ `开发工具、环境与工程实践`：Small Sticker 程序员主题贴纸在线商店
- 合并 `开发者工具讨论`（1 条；`small:developer-discussions`）→ `开发工具、环境与工程实践`：Hacker News讨论易用的网站页面截图生成工具
- 合并 `开发者搜索工具`（2 条；`small:developer-search`）→ `开发工具、环境与工程实践`：Goobe 面向程序员的技术资源互联网搜索引擎；Phind 面向开发者的人工智能技术问答搜索引擎
- 合并 `开发者站点`（1 条；`small:developer-sites`）→ `开发工具、环境与工程实践`：FuryIO 开源软件与开发者服务站点
- 合并 `开发者终端工具`（1 条；`small:developer-cli-tools`）→ `开发工具、环境与工程实践`：cmux 面向多任务工作的现代化终端应用
- 合并 `开发者资源导航`（2 条；`small:developer-resources`）→ `开发工具、环境与工程实践`：Free for Developers 开发者免费服务资源导航；程序员哈皮个人资源与技术内容分享网页
- 合并 `搜索服务开发`（1 条；`small:search-development`）→ `开发工具、环境与工程实践`：Meilisearch API 密钥创建与访问权限配置参考文档
- 合并 `数据库与工程实践`（2 条；`small:database-engineering`）→ `开发工具、环境与工程实践`：GitHub 工程团队升级 GitHub.com 至 MySQL 8.0 的实践；使用PostgreSQL简化整体技术栈的实践文章
- 合并 `数据库与搜索技术`（2 条；`small:databases-and-search`）→ `开发工具、环境与工程实践`：KeyDB高性能Redis兼容数据库官方文档；Manticore Search全文搜索引擎安装指南
- 合并 `文字与编码工具`（1 条；`small:text-and-encoding-tools`）→ `开发工具、环境与工程实践`：与熊论道熊曰文字加密与解密在线工具
- 合并 `桌面应用开发`（1 条；`small:desktop-development`）→ `开发工具、环境与工程实践`：Tauri 桌面应用开发的环境依赖与前置要求文档
- 合并 `消费电子产品数据库`（1 条；`small:consumer-product-databases`）→ `开发工具、环境与工程实践`：ChargeBaby：移动电源参数与型号查询数据库
- 合并 `物联网开发`（1 条；`small:iot-development`）→ `开发工具、环境与工程实践`：萤石开放平台 Android SDK 功能介绍文档
- 合并 `独立开发与创业社区`（1 条；`small:indie-development`）→ `开发工具、环境与工程实践`：独立开发者关于自我成长的社区讨论帖
- 合并 `独立开发与用户增长`（1 条；`small:indie-growth`）→ `开发工具、环境与工程实践`：Indie Hacker 前一千名用户获客渠道指南
- 合并 `独立开发者资源`（1 条；`small:indie-developer-resources`）→ `开发工具、环境与工程实践`：独立开发者出海所需技术栈与实用工具导航
- 合并 `电商开放平台`（1 条；`small:ecommerce-development`）→ `开发工具、环境与工程实践`：阿里巴巴1688开放平台开发者服务入口
- 合并 `研发协作工具`（1 条；`small:software-development-tools`）→ `开发工具、环境与工程实践`：TAPD一站式DevOps研发协作与项目管理方案
- 合并 `算法与编程练习`（2 条；`small:programming-practice`）→ `开发工具、环境与工程实践`：力扣LeetCode中文算法题库与编程学习平台；AmosCloud德科机试一星题编程练习题库
- 合并 `编程竞赛与在线评测`（1 条；`small:programming-contests`）→ `开发工具、环境与工程实践`：QDUOJ在线评测系统中的ACM竞赛排名页面
- 合并 `远程开发工作`（1 条；`small:remote-jobs`）→ `开发工具、环境与工程实践`：JS Remotely远程JavaScript开发工作资源平台

### `01_技术与AI / 编程开发与开源 / 开发文档、接口与参考`（7 个来源目录 / 9 条书签）
- 合并 `API与开发工具`（1 条；`small:api-tools`）→ `开发文档、接口与参考`：OpenList访问令牌获取与管理在线工具
- 合并 `创投融资数据库`（1 条；`small:venture-capital-databases`）→ `开发文档、接口与参考`：IT桔子中国创业公司与投融资数据服务平台
- 合并 `定位与地图 API`（1 条；`small:location-apis`）→ `开发文档、接口与参考`：基站WiFi与LBS经纬度定位数据查询接口
- 合并 `开发者 API 服务`（2 条；`small:developer-apis`）→ `开发文档、接口与参考`：和风天气开发者控制台应用配置与密钥管理；天行数据 TianAPI 开发者数据接口平台
- 合并 `开发速查资料`（2 条；`small:developer-reference`）→ `开发文档、接口与参考`：前端开发常用命令与工具快速参考手册；QuickRef多种编程语言与开发工具速查表集合
- 合并 `水草造景`（1 条；`small:aquascaping`）→ `开发文档、接口与参考`：IAPLC 世界水草造景大赛官方作品平台
- 合并 `编程知识参考`（1 条；`small:programming-reference`）→ `开发文档、接口与参考`：编程词典在线开发术语与技术知识查询工具

### `01_技术与AI / 编程开发与开源 / 开源项目与版本协作`（22 个来源目录 / 32 条书签）
- 合并 `Git 与版本控制工具`（2 条；`small:git-tools`）→ `开源项目与版本协作`：GitButler 面向开发者的 Git 分支与版本管理工具；Tower面向Mac与Windows的Git图形客户端
- 合并 `GitHub与开发者成长`（1 条；`small:github-and-career`）→ `开源项目与版本协作`：使用README美化GitHub个人主页的配置教程
- 合并 `Git与版本控制`（2 条；`small:git-and-version-control`）→ `开源项目与版本协作`：交互式学习 Git 分支操作与提交历史可视化教程；廖雪峰 Git 教程中的创建与合并分支操作指南
- 合并 `代码版本管理`（2 条；`small:source-control`）→ `开源项目与版本协作`：至恒融兴内部GitLab代码仓库登录入口；SourceTree跨平台Git图形化客户端官网
- 合并 `企业开源项目`（2 条；`small:enterprise-open-source`）→ `开源项目与版本协作`：人人开源社区的企业应用开发项目与技术资源；人人微服务 Renren Cloud 企业后台演示平台
- 合并 `开发效率工具`（2 条；`small:developer-productivity`）→ `开源项目与版本协作`：WakaTime 开发编码时长与项目统计面板；Astral GitHub Stars 收藏项目整理工具控制台
- 合并 `开发者数据与年度报告`（1 条；`small:developer-analytics`）→ `开源项目与版本协作`：YunYouJun 的 GitHub 年度趋势与开发活动总结
- 合并 `开发者桌面工具`（2 条；`small:developer-desktop-tools`）→ `开源项目与版本协作`：FileZilla跨平台FTP文件传输客户端完整下载页；Fork 跨平台快速友好的 Git 图形客户端
- 合并 `开发者活动`（1 条；`small:developer-events`）→ `开源项目与版本协作`：GitHub Universe 2022活动报名确认页面
- 合并 `开源协议与许可证`（1 条；`small:open-source-licensing`）→ `开源项目与版本协作`：开源软件许可证选择原则与常见协议对比讨论
- 合并 `开源应用项目`（1 条；`small:open-source-applications`）→ `开源项目与版本协作`：Halo 开源 Java 博客与内容管理系统官方网站
- 合并 `开源生态分析`（1 条；`small:open-source-analytics`）→ `开源项目与版本协作`：OSS Insight GitHub 开源项目趋势分析平台
- 合并 `开源社区`（2 条；`small:open-source-communities`）→ `开源项目与版本协作`：Open GitHub 社区开源项目交流与资源平台；Fresns v2.1.0 正式版发布公告与更新说明
- 合并 `开源项目与开发资讯`（1 条；`small:open-source-and-dev-news`）→ `开源项目与版本协作`：Open Source Daily开源项目与开发者资讯每日聚合站
- 合并 `开源项目与计划`（1 条；`small:open-source-programs`）→ `开源项目与版本协作`：Google Summer of Code谷歌开源之夏项目官网
- 合并 `开源项目与软件`（1 条；`small:open-source-projects`）→ `开源项目与版本协作`：TORCS开源赛车模拟器项目新闻与软件下载页面
- 合并 `开源项目发现`（1 条；`small:open-source-discovery`）→ `开源项目与版本协作`：Rising Repo筛选GitHub近期热门上升项目工具
- 合并 `数字商品与账号服务`（2 条；`small:digital-goods`）→ `开源项目与版本协作`：美区 Apple ID 与 Shadowrocket 兑换码购买网站汇总；Netflix 会员账号与海外 Apple ID 自动发卡页面
- 合并 `数字档案与网页存档`（1 条；`small:digital-archives`）→ `开源项目与版本协作`：Internet Archive免费数字图书影音与网页存档馆
- 合并 `数字游民与城市比较`（1 条；`small:digital-nomad-tools`）→ `开源项目与版本协作`：Nomad List全球数字游民城市生活成本比较平台
- 合并 `数字游民与海外生活`（2 条；`small:digital-nomads`）→ `开源项目与版本协作`：RW实验室数字游民社区与远程工作平台；TangTalk数字居民服务与海外生活咨询网站
- 合并 `数字金融服务`（2 条；`small:digital-finance`）→ `开源项目与版本协作`：Fiat24代币化支付与数字金融服务平台；Fiat24个人账户资产与支付管理仪表盘

### `02_学习与研究 / 论文、科研与学科资料 / 人文、社会与区域研究`（1 个来源目录 / 2 条书签）
- 合并 `旅行与地理内容`（2 条；`small:travel-and-geography`）→ `人文、社会与区域研究`：中国国家地理网深度旅行与自然地理内容平台；穷游论坛叙利亚伊拉克与罗贾瓦深度旅行纪实帖

### `02_学习与研究 / 论文、科研与学科资料 / 医学、生物与生命科学`（8 个来源目录 / 11 条书签）
- 合并 `健康与医学`（2 条；`small:health-medicine`）→ `医学、生物与生命科学`：医学微视权威医学专家健康科普视频平台；默沙东诊疗手册中文版权威医学健康知识库
- 合并 `健康与医疗技术`（1 条；`small:health-and-medical`）→ `医学、生物与生命科学`：Nightscout开源糖尿病血糖监测项目介绍
- 合并 `医疗机构与医生`（1 条；`small:medical-profiles`）→ `医学、生物与生命科学`：中山大学附属第七医院张影医生个人页面
- 合并 `医美医生信息`（1 条；`small:medical-aesthetics`）→ `医学、生物与生命科学`：新氧平台任东医生医美项目与评价信息
- 合并 `家族祖源基因分析`（2 条；`small:genealogy-dna`）→ `医学、生物与生命科学`：福建泉州晋江蔡氏祖源基因分析结果页面；湖南邵阳刘氏祖源基因分析结果页面
- 合并 `生物学学习资料`（1 条；`small:biology-study`）→ `医学、生物与生命科学`：玉米籽粒颜色遗传与自由授粉群体题目解析
- 合并 `生物学查询与参考`（1 条；`small:biology-reference`）→ `医学、生物与生命科学`：CFH中国生物物种名称综合在线查询
- 合并 `药品医疗器械监管`（2 条；`small:medical-regulation`）→ `医学、生物与生命科学`：国家药品监督管理局药品医疗器械监管官方网站；国家药监局国产医疗器械产品注册数据查询

### `02_学习与研究 / 论文、科研与学科资料 / 学科资料与研究参考`（20 个来源目录 / 25 条书签）
- 合并 `人工智能学术组织`（1 条；`small:artificial-intelligence`）→ `学科资料与研究参考`：中国人工智能学会官方网站与行业资讯
- 合并 `人格与心理测试`（1 条；`small:personality-tests`）→ `学科资料与研究参考`：16Personalities免费人格类型与性格测试
- 合并 `创意互动实验`（2 条；`small:creative-interactive`）→ `学科资料与研究参考`：BLACKOUT随音乐节奏变化的互动视听体验；Neal.fun设计下一代iPhone的互动网页体验
- 合并 `古籍与历史文献`（1 条；`small:rare-books`）→ `学科资料与研究参考`：书格中国古籍高清影像与文献搜索
- 合并 `咨询研究资源`（1 条；`small:consulting-resources`）→ `学科资料与研究参考`：咨询圈工作室共享咨询与行业研究资源网站
- 合并 `商业管理研究`（1 条；`small:business-management`）→ `学科资料与研究参考`：沃顿知识在线商业管理思想与研究文章平台
- 合并 `地理空间与遥感`（2 条；`small:geospatial-tools`）→ `学科资料与研究参考`：51Earth 地球观测与遥感应用服务平台；51Earth 平台服务申请与接入页面
- 合并 `学术英语与写作`（1 条；`small:english-writing`）→ `学科资料与研究参考`：OpenLearning学术英语写作在线课程
- 合并 `心理学与人格`（1 条；`small:psychology-and-personality`）→ `学科资料与研究参考`：MBTI十六型人格理论入门与性格类型介绍
- 合并 `敏感心理健康内容`（1 条；`small:sensitive-mental-health`）→ `学科资料与研究参考`：含自杀相关主题的个人博客文章页面
- 合并 `数据科学计算`（2 条；`small:data-science`）→ `学科资料与研究参考`：Project Jupyter 交互式数据科学计算平台；机器学习数据缺失混乱重复问题的数据清洗指南
- 合并 `汉字研究与字形资料`（1 条；`small:chinese-character-resources`）→ `学科资料与研究参考`：北京师范大学汉字全息资源应用系统入口
- 合并 `科学传播与媒体制作`（1 条；`small:science-media`）→ `学科资料与研究参考`：SciePro科学影视制作与科普内容服务
- 合并 `科学研究资讯`（1 条；`small:science-news`）→ `学科资料与研究参考`：ScienceDaily全球科研新闻与科学资讯
- 合并 `科技商业资讯与研究`（1 条；`small:tech-business-news`）→ `学科资料与研究参考`：钛媒体科技商业研究报告与产业创新资讯
- 合并 `科技科普资讯`（2 条；`small:science-technology-media`）→ `学科资料与研究参考`：果壳网泛科技科普内容与交流社区；科学松鼠会面向大众的科学传播网站
- 合并 `科普与知识文章`（1 条；`small:science-explainers`）→ `学科资料与研究参考`：HowStuffWorks万物工作原理科普知识网站
- 合并 `管理研究报告与文档`（1 条；`small:management-reports`）→ `学科资料与研究参考`：MBA智库文档商业管理研究报告与知识资料库
- 合并 `计算机科学项目`（1 条；`small:computer-science-programs`）→ `学科资料与研究参考`：KAUST计算机科学项目课程研究与招生信息
- 合并 `论文查重检测`（2 条；`small:plagiarism-checking`）→ `学科资料与研究参考`：PaperPass学术论文重复率检测平台；CNKI知网论文重复率检测服务入口

### `02_学习与研究 / 论文、科研与学科资料 / 理工、工程与自然科学`（7 个来源目录 / 10 条书签）
- 合并 `办公空间服务`（1 条；`small:workspace-services`）→ `理工、工程与自然科学`：WeWork 联合办公空间与会议室预订服务
- 合并 `宇宙与太空模拟`（1 条；`small:space-simulation`）→ `理工、工程与自然科学`：SpaceEngine 宇宙空间模拟器软件下载与介绍页面
- 合并 `数学与工程软件`（2 条；`small:math-and-engineering`）→ `理工、工程与自然科学`：MathWorks在线安装MATLAB与Simulink产品指南；MathWorks账户登录与MATLAB软件下载入口
- 合并 `数学与集合论讨论`（1 条；`small:mathematics-discussions`）→ `理工、工程与自然科学`：自然数函数集合不可数性的集合论证明讨论
- 合并 `数学公式工具`（1 条；`small:math-tools`）→ `理工、工程与自然科学`：LaTeX Live 在线数学公式编辑与预览工具
- 合并 `数学学习资料`（2 条；`small:math-learning`）→ `理工、工程与自然科学`：玄数网站三角函数与反三角函数图像教程；玄数网站三角函数基础概念与图像教程
- 合并 `文档与公式工具`（2 条；`small:document-and-math-tools`）→ `理工、工程与自然科学`：MathType数学公式编辑器中文官网与使用教程；Mathpix Snipping Tool数学公式识别工具下载页面

### `02_学习与研究 / 论文、科研与学科资料 / 论文、文献与科研工具`（43 个来源目录 / 54 条书签）
- 合并 `AI 研究与演示`（1 条；`small:ai-research`）→ `论文、文献与科研工具`：NVIDIA Research在线深度学习交互演示平台
- 合并 `AI 研究项目`（2 条；`small:ai-research-projects`）→ `论文、文献与科研工具`：Abel生成式人工智能数学推理能力研究项目；IDE-3D高分辨率人像交互式编辑项目
- 合并 `AI新闻与研究`（1 条；`small:ai-news-research`）→ `论文、文献与科研工具`：上海人工智能实验室发布AI高考评测结果
- 合并 `SaaS 市场分析`（1 条；`small:saas-market-research`）→ `论文、文献与科研工具`：WhoisMaking Money 基于 Stripe 支付流量的营收分析工具
- 合并 `VPN 市场调研`（1 条；`small:vpn-research`）→ `论文、文献与科研工具`：白鲸出海平台VPN应用市场搜索结果
- 合并 `互联网数据研究`（2 条；`small:internet-research`）→ `论文、文献与科研工具`：199IT中文互联网数据研究资讯中心首页；比达咨询互联网大数据行业研究报告平台
- 合并 `人工智能研究机构`（2 条；`small:ai-research-labs`）→ `论文、文献与科研工具`：马克斯普朗克智能系统研究所感知系统研究部门；Matthias Nießner实验室视觉计算与人工智能研究
- 合并 `企业工商查询`（1 条；`small:company-research`）→ `论文、文献与科研工具`：东莞市三基音响科技有限公司工商信用信息
- 合并 `信息通信研究报告`（1 条；`small:ict-research-reports`）→ `论文、文献与科研工具`：中国信通院科研能力权威发布白皮书页面
- 合并 `创投与私募市场研究`（2 条；`small:venture-capital-research`）→ `论文、文献与科研工具`：投中网中国创业投资私募股权行业资讯数据平台；清科研究中心私募股权与创投市场研究报告
- 合并 `医疗健康行业研究`（1 条；`small:healthcare-industry-research`）→ `论文、文献与科研工具`：动脉网医疗健康产业研究报告与创新资讯
- 合并 `品牌研究与榜单`（1 条；`small:brand-research`）→ `论文、文献与科研工具`：Chnbrand BrandKey中国顾客满意度指数榜单
- 合并 `商业数据与行业研究`（1 条；`small:business-data-research`）→ `论文、文献与科研工具`：镝数聚商业数据可视化与行业研究服务平台
- 合并 `大数据与营销研究`（1 条；`small:data-marketing-research`）→ `论文、文献与科研工具`：国双数据中心数字营销与大数据研究报告
- 合并 `学术 AI 搜索与问答`（1 条；`small:academic-ai-search`）→ `论文、文献与科研工具`：Consensus基于论文证据的智能问答搜索平台
- 合并 `学术文献工具`（2 条；`small:research-literature`）→ `论文、文献与科研工具`：ReadPaper论文阅读与学术讨论社区平台；Connected Papers学术文献关系可视化探索工具
- 合并 `学术职业发展`（1 条；`small:academic-careers`）→ `论文、文献与科研工具`：蒂宾根大学博士后与青年研究人员职业发展机会
- 合并 `新经济产业研究`（1 条；`small:new-economy-research`）→ `论文、文献与科研工具`：亿欧智库新经济产业研究报告与商业资讯
- 合并 `新能源与气候研究`（1 条；`small:energy-and-climate-research`）→ `论文、文献与科研工具`：BloombergNEF全球新能源转型与气候金融研究机构
- 合并 `旅游行业研究`（1 条；`small:travel-industry-research`）→ `论文、文献与科研工具`：旅游圈旅游行业数据专题与市场研究资讯
- 合并 `求职调研与薪资评价`（1 条；`small:job-research`）→ `论文、文献与科研工具`：Glassdoor公司薪资评价与职位搜索平台
- 合并 `汽车评测与研究`（1 条；`small:automotive-research`）→ `论文、文献与科研工具`：中国保险汽车安全指数车型碰撞测试结果查询
- 合并 `消费者与市场研究`（1 条；`small:consumer-market-research`）→ `论文、文献与科研工具`：尼尔森中国消费者洞察与市场测量研究服务
- 合并 `用户研究与可用性测试`（1 条；`small:user-research`）→ `论文、文献与科研工具`：UserCrowd用户测试平台手机号码验证页面
- 合并 `电子书与学术资料`（1 条；`small:ebooks-and-research`）→ `论文、文献与科研工具`：Library Genesis电子书与学术资料检索站
- 合并 `研究与信息检索`（1 条；`small:research-tools`）→ `论文、文献与科研工具`：Tavily Company Research 企业信息检索研究工具
- 合并 `研究项目与实验室`（2 条；`small:research-labs`）→ `论文、文献与科研工具`：维尔茨堡大学法学院CDSV研究项目团队成员；KAUST生物本体研究组研究方向与团队信息
- 合并 `科研创新机构`（2 条；`small:research-and-innovation`）→ `论文、文献与科研工具`：山东产业技术研究院烟台创新研发服务平台；中国管理科学研究院行业发展研究所官方网站
- 合并 `科研实验室服务`（1 条；`small:research-supplies`）→ `论文、文献与科研工具`：探索平台科研试剂耗材仪器与实验室服务
- 合并 `科研机构排名`（1 条；`small:research-rankings`）→ `论文、文献与科研工具`：Nature Index亚太地区2021年度科研机构排名
- 合并 `科研知识资源`（1 条；`small:research-knowledge`）→ `论文、文献与科研工具`：MiniManuscript 维基式学术论文与研究知识百科
- 合并 `科研资讯`（2 条；`small:research-news`）→ `论文、文献与科研工具`：搜学术学术圈科研热点与学界新闻聚合；中国学术会议在线会议论文与会议信息平台
- 合并 `科研项目申报`（1 条；`small:research-funding`）→ `论文、文献与科研工具`：全国哲学社会科学工作办公室项目申报与成果管理官网
- 合并 `移动互联网数据研究`（2 条；`small:mobile-data-research`）→ `论文、文献与科研工具`：Trustdata移动互联网数据洞察与行业研究报告；极光移动应用数据研究报告与用户行为洞察
- 合并 `移动应用安全研究`（1 条；`small:mobile-security-research`）→ `论文、文献与科研工具`：安卓微信聊天信息本地存储机制解析文章
- 合并 `经济研究与资讯`（1 条；`small:economics-research`）→ `论文、文献与科研工具`：中国人民大学中国宏观经济论坛 CMF 官方网站
- 合并 `经济研究机构`（1 条；`small:economic-research-institutes`）→ `论文、文献与科研工具`：北京方迪经济发展研究院资讯与研究成果页面
- 合并 `网络审查研究`（2 条；`small:internet-censorship-research`）→ `论文、文献与科研工具`：积至与MESA实验室文件泄露深度分析报告；中国区域性防火墙相关泄露分析章节
- 合并 `苹果设备参数查询`（2 条；`small:apple-device-research`）→ `论文、文献与科研工具`：HubWeb苹果产品参数与规格查询中心；MacRumors苹果设备购买时机与换代周期指南
- 合并 `行业与市场研究`（1 条；`small:market-research`）→ `论文、文献与科研工具`：中国产业信息网行业前景与投资趋势研究报告
- 合并 `计算机科研团队`（1 条；`small:computer-research`）→ `论文、文献与科研工具`：南京大学PASCAL教学与程序分析研究团队主页
- 合并 `金融研究工具`（1 条；`small:finance-research-tools`）→ `论文、文献与科研工具`：研报问小白面向投资研究报告的专业AI搜索工具
- 合并 `食品产业研究`（1 条；`small:food-industry-research`）→ `论文、文献与科研工具`：湖南省小龙虾市场全景调查与发展前景预测报告

### `02_学习与研究 / 阅读、语言与通识学习 / 书籍、阅读与文学`（14 个来源目录 / 20 条书签）
- 合并 `AI 阅读工具`（1 条；`small:ai-reading`）→ `书籍、阅读与文学`：ChatBook AI 图书对话阅读与智能问答产品概览
- 合并 `PDF 与电子书工具`（2 条；`small:pdf-and-ebooks`）→ `书籍、阅读与文学`：PDF Drive在线PDF电子书搜索下载平台；PDFescape免费在线PDF编辑与表单填写工具
- 合并 `书签与搜索导航`（2 条；`small:bookmark-and-search`）→ `书籍、阅读与文学`：Raindrop.io 个人书签收藏与分类管理平台；虫部落聚合搜索引擎与资源检索导航
- 合并 `书签与网址导航`（1 条；`small:bookmark-discovery`）→ `书籍、阅读与文学`：书签地球浏览器书签共享与发现平台
- 合并 `书评与读书资源`（1 条；`small:book-review-resources`）→ `书籍、阅读与文学`：读写人书评杂志博客与读书资源聚合平台
- 合并 `文学作品资源`（1 条；`small:literature-resources`）→ `书籍、阅读与文学`：诺贝尔文学奖作品PDF文件访问分享页面
- 合并 `旅行预订`（2 条；`small:travel-booking`）→ `书籍、阅读与文学`：携程旅行酒店机票预订与旅游度假服务；Booking.com 东京酒店住宿搜索与预订结果
- 合并 `电子书管理`（1 条；`small:ebook-tools`）→ `书籍、阅读与文学`：Calibre 开源电子书管理、转换与阅读软件官网
- 合并 `编程书籍与技术资源`（2 条；`small:programming-books`）→ `书籍、阅读与文学`：SaltTiger 每日编程技术书籍与学习资源分享；SaltTiger 网站收录的 Java 编程书籍资源
- 合并 `语言与阅读学习`（2 条；`small:language-reading-learning`）→ `书籍、阅读与文学`：Spreeder CX 在线快速外文阅读训练工具；多邻国趣味化免费英语语言学习平台
- 合并 `软件工程阅读`（2 条；`small:software-engineering-reading`）→ `书籍、阅读与文学`：合格程序员推荐阅读书单与技术书籍指南；重构大师中文版常用软件设计模式图文指南
- 合并 `阅读与书签管理`（1 条；`small:reading-and-bookmarks`）→ `书籍、阅读与文学`：Omnivore 稍后阅读与网页内容收藏服务
- 合并 `阅读与书籍博客`（1 条；`small:reading-blogs`）→ `书籍、阅读与文学`：恩京书房个人阅读与书籍分享网站
- 合并 `阅读写作社区`（1 条；`small:reading-writing-communities`）→ `书籍、阅读与文学`：十五言高质量阅读写作与思想交流社区

### `02_学习与研究 / 阅读、语言与通识学习 / 文化、艺术与通识资料`（4 个来源目录 / 4 条书签）
- 合并 `传统文化与命理`（1 条；`small:traditional-culture`）→ `文化、艺术与通识资料`：中国传统风水方位与命理在线测算工具
- 合并 `文化创意站点`（1 条；`small:culture-sites`）→ `文化、艺术与通识资料`：大衆SHOCK堂独立文化与创意内容展示网站
- 合并 `日本文化`（1 条；`small:japanese-culture`）→ `文化、艺术与通识资料`：刀剑世界日本刀历史知识与名刀资料网站
- 合并 `美食与饮食文化`（1 条；`small:food-culture`）→ `文化、艺术与通识资料`：TasteAtlas全球地方美食与食材百科检索

### `02_学习与研究 / 阅读、语言与通识学习 / 语言、翻译与词典`（7 个来源目录 / 7 条书签）
- 合并 `编程语言学习`（1 条；`small:programming-languages`）→ `语言、翻译与词典`：网道 C 语言基本语法中文教程
- 合并 `编程语言环境`（1 条；`small:language-runtimes`）→ `语言、翻译与词典`：RubyInstaller Windows平台Ruby安装包下载页面
- 合并 `英语词典`（1 条；`small:english-dictionaries`）→ `语言、翻译与词典`：Merriam-Webster词典中nickname单词释义页面
- 合并 `语文与古诗文学习`（1 条；`small:chinese-language-learning`）→ `语言、翻译与词典`：古诗文网高中阶段文言文原文翻译大全
- 合并 `语言学习与翻译`（1 条；`small:language-tools`）→ `语言、翻译与词典`：爱粤语在线粤语翻译与学习辅助工具
- 合并 `语言能力测评`（1 条；`small:language-assessment`）→ `语言、翻译与词典`：中文词汇量在线测试与语言能力评估
- 合并 `转录与翻译工具`（1 条；`small:transcription-translation`）→ `语言、翻译与词典`：Transcription Hub 在线翻译应用服务页面

### `02_学习与研究 / 阅读、语言与通识学习 / 通识学习与参考资源`（28 个来源目录 / 37 条书签）
- 合并 `AI 学习资源`（2 条；`small:ai-resources`）→ `通识学习与参考资源`：鱼皮AI知识库收录的DeepSeek开源项目；DeepSeek官方模型应用与工具整理指南
- 合并 `AI 文档阅读与分析`（1 条；`small:ai-document-tools`）→ `通识学习与参考资源`：ChatDOC通过人工智能对话阅读分析文档工具
- 合并 `AI学习资源`（1 条；`small:ai-learning-resources`）→ `通识学习与参考资源`：Anthropic 官方课程与 AI 产品学习平台
- 合并 `Go语言开发`（1 条；`small:go-programming`）→ `通识学习与参考资源`：Go语言设计与实现：运行时、编译器和并发机制解析
- 合并 `Mac使用教程`（1 条；`small:macos-tutorials`）→ `通识学习与参考资源`：国行Mac启用Apple Intelligence功能的操作教程
- 合并 `NAS自托管与部署教程`（1 条；`small:nas-self-hosting`）→ `通识学习与参考资源`：飞牛fnOS通过Docker部署Immich人脸识别相册教程
- 合并 `Notion教程与资源`（1 条；`small:notion-resources`）→ `通识学习与参考资源`：Notion进度条制作教程及三种样式公式示例
- 合并 `Python爬虫教程`（1 条；`small:python-web-scraping`）→ `通识学习与参考资源`：Python爬取个人微博主页图片的一键追星教程
- 合并 `Windows 软件与教程`（2 条；`small:windows-software-guides`）→ `通识学习与参考资源`：2021年精选20款电脑装机必备软件资源包；Windows十破解应用商店付费应用教程
- 合并 `代理配置教程`（2 条；`small:proxy-configuration-guides`）→ `通识学习与参考资源`：科学上网配置教程：VMess、WebSocket 与 CDN；免流网络混淆参数配置参考文章
- 合并 `公共图书馆资源`（2 条；`small:public-libraries`）→ `通识学习与参考资源`：浙江图书馆馆藏资源与读者服务网站；广西壮族自治区图书馆资源服务网站
- 合并 `区块链测试网教程`（2 条；`small:blockchain-testnets`）→ `通识学习与参考资源`：Aptos激励测试网第三期零成本节点搭建教程；Sui测试网项目交互与激励参与操作教程
- 合并 `古籍与传统文化`（1 条；`small:chinese-classics`）→ `通识学习与参考资源`：识典古籍：在线阅读与检索中国传统古籍文献平台
- 合并 `图书馆服务`（1 条；`small:library-services`）→ `通识学习与参考资源`：浙江图书馆网借平台在线图书借阅服务入口
- 合并 `图书馆电子资源`（1 条；`small:library-resources`）→ `通识学习与参考资源`：广西图书馆电子资源平台在线浏览入口
- 合并 `学习与练习工具`（1 条；`small:learning-and-practice`）→ `通识学习与参考资源`：Monkeytype 在线打字速度测试与键盘练习工具
- 合并 `学习平台服务`（1 条；`small:learning-services`）→ `通识学习与参考资源`：科技强国学习平台辅助访问网站
- 合并 `数字图书与古籍`（1 条；`small:digital-library`）→ `通识学习与参考资源`：书格在线古籍与艺术图书数字阅览资源网站
- 合并 `文化产业统计数据`（1 条；`small:cultural-industry-statistics`）→ `通识学习与参考资源`：2023年前三季度全国文化产业企业营业收入统计数据
- 合并 `文化遗产与数字档案`（2 条；`small:cultural-archives`）→ `通识学习与参考资源`：台北故宫博物院开放资料与数位典藏图像集；Europeana欧洲文化遗产数字图书馆与馆藏检索
- 合并 `本地化翻译工具`（1 条；`small:localization-tools`）→ `通识学习与参考资源`：Crowdin 团队协作本地化翻译管理平台
- 合并 `机器学习文档`（2 条；`small:machine-learning-docs`）→ `通识学习与参考资源`：TensorFlow在Windows系统上的安装配置教程；Keras优化器API参考文档与训练算法说明
- 合并 `电子阅读器与 Kindle 指南`（2 条；`small:ereader-guides`）→ `通识学习与参考资源`：书伴Kindle阅读技巧电子书与设备使用指南；狗耳朵BBC内容推送至Kindle阅读服务
- 合并 `编程语言趋势`（1 条；`small:programming-trends`）→ `通识学习与参考资源`：TIOBE编程语言流行度指数排行榜
- 合并 `网络代理部署教程`（1 条；`small:network-proxy-guides`）→ `通识学习与参考资源`：Xray VPS 代理服务一键部署配置教程
- 合并 `自学资源导航`（1 条；`small:self-learning-resources`）→ `通识学习与参考资源`：学吧导航综合在线自学资源网站入口
- 合并 `自然语言处理与AI`（1 条；`small:nlp-and-ai`）→ `通识学习与参考资源`：JioNLP中文自然语言处理工具包的DeepWiki文档
- 合并 `虚拟文化遗产`（2 条；`small:virtual-heritage`）→ `通识学习与参考资源`：Mused古代遗址与博物馆虚拟游览平台；Mused虚拟游览埃及奈费尔塔里王后墓

### `02_学习与研究 / 高校、留学与考试 / 学习课程与教育资源`（21 个来源目录 / 29 条书签）
- 合并 `ASU课程资料`（2 条；`small:asu-courses`）→ `学习课程与教育资源`：ASU FSE 598 新兴计算技术课程主页与导览；ASU FSE 501 技术创业课程在线阅读书单
- 合并 `GMAT考试与留学`（2 条；`small:gmat-preparation`）→ `学习课程与教育资源`：申请方解读GMAT考试用途与适用人群；新东方GMAT考试基础介绍与备考说明
- 合并 `IT职业认证`（1 条；`small:it-certification`）→ `学习课程与教育资源`：CIW中国认证官方网站与认证项目导航
- 合并 `NFT学习课程`（2 条；`small:nft-learning`）→ `学习课程与教育资源`：101 Blockchains NFT基础知识在线课程；Skillshare面向新手的NFT铸造与销售课程
- 合并 `个人证书与定制服务`（1 条；`small:personal-services`）→ `学习课程与教育资源`：Michael Sutter 学历证书装裱服务登录页面
- 合并 `人工智能博士项目`（1 条；`small:ai-doctoral-programs`）→ `学习课程与教育资源`：Science of Intelligence人工智能博士培养项目介绍
- 合并 `博士职位与科研招聘`（1 条；`small:phd-jobs`）→ `学习课程与教育资源`：于利希研究中心能源系统高性能计算博士职位招聘
- 合并 `博士资助与奖学金`（2 条；`small:doctoral-funding`）→ `学习课程与教育资源`：汉堡大学博士阶段资助渠道与研究经费说明；KAUST全额资助硕士博士奖学金与助学金说明
- 合并 `国际教育交流`（1 条；`small:international-education`）→ `学习课程与教育资源`：中国东盟教育交流周主题活动资讯平台
- 合并 `在线教育课程`（1 条；`small:online-education`）→ `学习课程与教育资源`：Sophia Learning 提供大学学分在线课程学习平台
- 合并 `在线课程制作工具`（1 条；`small:elearning-authoring`）→ `学习课程与教育资源`：iSpring Suite在线课程制作软件免费试用下载
- 合并 `大学生就业创业`（2 条；`small:student-career`）→ `学习课程与教育资源`：全国大学生创业服务网项目与赛事平台首页；国家大学生就业服务平台个人设置页面
- 合并 `学生优惠与教育资源`（1 条；`small:student-benefits`）→ `学习课程与教育资源`：高校师生Apple微软GitHub等产品教育优惠购买指南
- 合并 `安全生产职业培训`（2 条；`small:workplace-safety-training`）→ `学习课程与教育资源`：中国安全生产协会新职业网络学院官方网站首页；企业安全管理岗位能力提升培训班举办通知
- 合并 `广东教育考试`（1 条；`small:guangdong-education`）→ `学习课程与教育资源`：广东省教育考试院官方网站与考试服务入口
- 合并 `性教育参考`（1 条；`small:sexual-health`）→ `学习课程与教育资源`：性爱姿势图文与动图科普参考网站
- 合并 `机器学习课程`（1 条；`small:machine-learning-courses`）→ `学习课程与教育资源`：斯坦福CS224W图机器学习课程资料与课程主页
- 合并 `职业认证与培训`（1 条；`small:professional-certification`）→ `学习课程与教育资源`：CIW中国认证会员俱乐部加入与认证服务页面
- 合并 `职业资格认证查询`（2 条；`small:professional-certification-services`）→ `学习课程与教育资源`：职业技能等级评价机构公示信息官方查询系统；全国专业技术人员职业资格证书查询验证系统登录
- 合并 `雅思备考与模拟考试`（2 条；`small:ielts-prep`）→ `学习课程与教育资源`：雅思真题在线模拟测试与答案解析库；雅思免费机考模拟考试注册与练习系统
- 合并 `高校官网`（1 条；`small:universities`）→ `学习课程与教育资源`：四川大学官方网站综合门户首页

### `02_学习与研究 / 高校、留学与考试 / 教育政策与职业教育`（14 个来源目录 / 17 条书签）
- 合并 `产教融合动态`（2 条；`small:industry-education-integration`）→ `教育政策与职业教育`：浙江传媒学院再次获批现代产业学院建设点新闻；山东文化产业职业学院加入数字创意AIGC产教融合共同体
- 合并 `产教融合资料`（1 条；`small:industry-education-cooperation`）→ `教育政策与职业教育`：南昌职业大学产学合作项目与校企合作手册
- 合并 `山东教育政务`（1 条；`small:shandong-education-government`）→ `教育政策与职业教育`：枣庄市教育局官方网站教育政务与通知公告
- 合并 `工程教育合作`（1 条；`small:engineering-education-cooperation`）→ `教育政策与职业教育`：天津大学ACNET工程技术教育认证合作项目网站
- 合并 `教育与专业择校`（1 条；`small:education-guidance`）→ `教育政策与职业教育`：2024年全国新能源科学与工程专业开设高校排名查询
- 合并 `教育与大学`（1 条；`small:education-universities`）→ `教育政策与职业教育`：博伊西州立大学官方网站与教育资源入口
- 合并 `教育产业投资`（1 条；`small:education-investment`）→ `教育政策与职业教育`：多鲸资本教育产业投资机构官方网站
- 合并 `教育产业资讯`（1 条；`small:education-industry`）→ `教育政策与职业教育`：芥末堆教育产业资讯与教育创新观察平台
- 合并 `教育学术研究`（1 条；`small:education-research`）→ `教育政策与职业教育`：中国社会科学杂志社教育学频道学术资讯页面
- 合并 `教育政策与数字化`（2 条；`small:education-policy`）→ `教育政策与职业教育`：怀进鹏谈国家教育数字化资源中心与服务平台建设进展；共建一带一路国家教育合作愿景与发展方向解读
- 合并 `教育服务查询`（1 条；`small:education-services`）→ `教育政策与职业教育`：强国助学学习情况与相关信息在线查询系统
- 合并 `教育机构与院校`（2 条；`small:education-institutions`）→ `教育政策与职业教育`：山东理工大学继续教育学院学院简介；淄博市技师学院信息工程系官方网站
- 合并 `教育科研网络`（1 条；`small:education-networks`）→ `教育政策与职业教育`：中国教育和科研计算机网CERNET官方网站首页
- 合并 `教育认证机构`（1 条；`small:education-accreditation`）→ `教育政策与职业教育`：悉尼协议研究院单位简介与机构信息

### `02_学习与研究 / 高校、留学与考试 / 留学、签证与海外发展`（12 个来源目录 / 16 条书签）
- 合并 `出入境与证件`（1 条；`small:immigration-travel-documents`）→ `留学、签证与海外发展`：国家移民管理局官方网站与出入境政务服务入口
- 合并 `出国留学服务`（1 条；`small:study-abroad-services`）→ `留学、签证与海外发展`：51offer英国澳洲美国出国留学申请服务平台
- 合并 `奖学金申请`（1 条；`small:scholarships`）→ `留学、签证与海外发展`：亚利桑那州立大学ScholarshipUniverse奖学金匹配系统
- 合并 `学历认证与院校认可`（2 条；`small:degree-recognition`）→ `留学、签证与海外发展`：德国anabin外国学历与院校认证信息查询门户；anabin国外高等教育机构认可状态数据库查询
- 合并 `工作签证与海外就业`（1 条；`small:work-visas`）→ `留学、签证与海外发展`：新加坡人力部就业准证Employment Pass申请说明
- 合并 `日本旅行签证`（2 条；`small:japan-travel-visa`）→ `留学、签证与海外发展`：日本驻华大使馆赴日旅游签证申请指南；日本国驻华大使馆赴日签证咨询官方指南
- 合并 `日本签证与移民`（1 条；`small:japan-immigration`）→ `留学、签证与海外发展`：日本护照免签国家与居留签证移民资料查询
- 合并 `海外博士申请`（1 条；`small:phd-study-abroad`）→ `留学、签证与海外发展`：特拉维夫大学中文官网博士研究生申请信息
- 合并 `海外工作与移民`（1 条；`small:immigration-work`）→ `留学、签证与海外发展`：奥地利红白红卡工作居留许可申请说明
- 合并 `海外生活与签证`（2 条；`small:travel-immigration`）→ `留学、签证与海外发展`：数字游民签证覆盖四十余国的申请攻略大全；润去新加坡留学就业移民信息服务平台主页
- 合并 `美国税务与身份`（1 条；`small:us-tax-immigration`）→ `留学、签证与海外发展`：美国国税局 IRS 个人报税识别号码 ITIN 申请指南
- 合并 `香港优才与签证`（2 条；`small:hong-kong-immigration`）→ `留学、签证与海外发展`：香港入境处优秀人才入境计划官方说明页面；香港政府优秀人才入境计划网上申请服务指南

### `02_学习与研究 / 高校、留学与考试 / 考试、证书与职业资格`（9 个来源目录 / 14 条书签）
- 合并 `学历学籍与认证`（2 条；`small:education-credentials`）→ `考试、证书与职业资格`：美国国家学生信息中心学历与学籍服务官网；美国国务院学术证书认证办理官方说明
- 合并 `学历认证服务`（2 条；`small:education-credentialing`）→ `考试、证书与职业资格`：中国留学服务中心学历学位认证在线服务平台；教育部留学服务中心国外学历认证院校查询入口
- 合并 `教师资格认定`（1 条；`small:teacher-certification`）→ `考试、证书与职业资格`：郴州市2023年第二批高中中职教师资格认定结果公告
- 合并 `数字证书服务`（1 条；`small:certificate-services`）→ `考试、证书与职业资格`：数字证书在线预受理系统管理后台入口
- 合并 `考研公共课资料`（1 条；`small:graduate-exam`）→ `考试、证书与职业资格`：2018年全国硕士研究生政治真题及答案解析
- 合并 `考试题库与备考`（2 条；`small:exam-preparation`）→ `考试、证书与职业资格`：粉笔题库考试练习题与学习资料下载平台；考试资料网试题题库与答案检索首页
- 合并 `职业考试与刷题`（2 条；`small:exam-prep`）→ `考试、证书与职业资格`：F7职业技能鉴定考试题库与答案解析；物质与运动关系唯心主义错误试题解析
- 合并 `职业能力证书`（2 条；`small:professional-certificates`）→ `考试、证书与职业资格`：工信部教育与考试中心职业能力证书项目介绍；工业和信息化部人才交流中心证书服务页面
- 合并 `计算机等级考试`（1 条；`small:computer-exams`）→ `考试、证书与职业资格`：中国教育考试网全国计算机等级考试NCRE入口

### `02_学习与研究 / 高校、留学与考试 / 高校、院校与校园服务`（26 个来源目录 / 36 条书签）
- 合并 `中学招生与升学信息`（2 条；`small:secondary-school-admissions`）→ `高校、院校与校园服务`：九江一中2015年金榜题名学生名册；九江一中2018年招生简章与办学发展介绍
- 合并 `中职招生查询`（2 条；`small:secondary-vocational-admissions`）→ `高校、院校与校园服务`：湖南省中等职业教育阳光招生信息平台郴州学校查询；湖南省中等职业教育阳光招生平台学校信息详情
- 合并 `大学与校园生活`（1 条；`small:university-life`）→ `高校、院校与校园服务`：亚利桑那州立大学ASU官方服饰与纪念品商城
- 合并 `大学与课程资源`（1 条；`small:university-resources`）→ `高校、院校与校园服务`：ClassReview亚利桑那州立大学课程评价与选课参考
- 合并 `大学专业与课程查询`（1 条；`small:university-study-search`）→ `高校、院校与校园服务`：明斯特大学中央学习咨询专业课程查询页面
- 合并 `大学新闻与动态`（1 条；`small:university-news`）→ `高校、院校与校园服务`：哥廷根大学新闻与校园动态发布页面
- 合并 `大学课程资料`（1 条；`small:university-courses`）→ `高校、院校与校园服务`：亚利桑那州立大学MEng组合项目报告说明录像
- 合并 `山东技术学院`（2 条；`small:shandong-technical-college`）→ `高校、院校与校园服务`：山东技术学院学院概况与办学情况介绍；山东技术学院招生信息网与报考资讯入口
- 合并 `山东招生考试`（2 条；`small:shandong-admissions-exams`）→ `高校、院校与校园服务`：山东省教育招生考试院官方网站考试招生服务入口；山东2019年初中后高职高师分市招生计划通知
- 合并 `山东理工大学招生`（1 条；`small:shandong-university-admissions`）→ `高校、院校与校园服务`：山东理工大学硕士研究生招生信息与公告
- 合并 `山东理工大学资料`（2 条；`small:shandong-university-info`）→ `高校、院校与校园服务`：山东理工大学工作简介与部门职责说明；山东理工大学计算机科学与技术学院官方网站
- 合并 `山东职业院校招生`（2 条；`small:shandong-vocational-admissions`）→ `高校、院校与校园服务`：山东文化产业职业学院招生信息官方网站；聊城职业技术学院2023年山东省招生计划设置
- 合并 `山东高校招生`（1 条；`small:shandong-college-admissions`）→ `高校、院校与校园服务`：枣庄学院2023年分专业招生计划查询
- 合并 `教育与招生信息`（1 条；`small:education-admissions`）→ `高校、院校与校园服务`：烟台工程职业技术学院普通高考招生信息页面
- 合并 `校园生活社区`（1 条；`small:campus-communities`）→ `高校、院校与校园服务`：大学生活质量指北高校体验与信息交流社区
- 合并 `校园网络服务`（1 条；`small:campus-it`）→ `高校、院校与校园服务`：同济大学校园VPN常见问题官方解答
- 合并 `校园资源导航`（1 条；`small:campus-navigation`）→ `高校、院校与校园服务`：北邮人BYR校园资源与网站导航入口
- 合并 `湖北职业院校招生`（2 条；`small:hubei-vocational-admissions`）→ `高校、院校与校园服务`：鄂州职业大学2023年招生报名入口与招生信息；2023年湖北省中职中专招生官方网站报名入口
- 合并 `烟台职业教育政务`（1 条；`small:yantai-vocational-education`）→ `高校、院校与校园服务`：烟台市政府门户网站职业教育专题信息页面
- 合并 `职业学校名录`（2 条；`small:vocational-school-directories`）→ `高校、院校与校园服务`：广州市增城区公办职业学校名单一览表；上饶市公布2023年中职学历教育招生资格学校名单通知
- 合并 `职业教育行业资讯`（2 条；`small:vocational-education-industry`）→ `高校、院校与校园服务`：现代职业教育网职业教育行业资讯平台；中国职业教育与成人教育行业资讯网站
- 合并 `职业教育集团`（1 条；`small:vocational-education-groups`）→ `高校、院校与校园服务`：尚职教育集团官方网站与职业教育业务信息
- 合并 `高校发展规划`（1 条；`small:college-development-plans`）→ `高校、院校与校园服务`：山东水利职业学院十四五事业发展规划全文
- 合并 `高校学习平台`（2 条；`small:university-learning`）→ `高校、院校与校园服务`：集美大学超星网络教学平台入口；Unipus高校外语教学与学习平台首页
- 合并 `高校教育与实践`（1 条；`small:university-education`）→ `高校、院校与校园服务`：厦门工学院工程坊实践教学平台页面
- 合并 `高校资产经营资讯`（1 条；`small:university-corporate-news`）→ `高校、院校与校园服务`：南昌大学资产经营有限责任公司新闻公告页面

### `03_创作与娱乐 / 内容创作与社交媒体 / 内容创作与社区资源`（1 个来源目录 / 2 条书签）
- 合并 `公众号文章导出工具`（2 条；`small:wechat-article-tools`）→ `内容创作与社区资源`：微信公众号文章批量导出为Word和PDF的在线助手；微信公众号文章导出为PDF和Word的软件介绍页面

### `03_创作与娱乐 / 内容创作与社交媒体 / 社交平台与内容运营`（9 个来源目录 / 12 条书签）
- 合并 `哔哩哔哩下载工具`（2 条；`small:bilibili-download-tools`）→ `社交平台与内容运营`：唧唧下载电脑客户端与哔哩哔哩视频下载；爱哔哩哔哩视频与音频 MP3 解析下载站
- 合并 `社交与交友`（1 条；`small:social-dating`）→ `社交平台与内容运营`：Tinder在线约会交友与认识新朋友平台
- 合并 `社交与聊天平台`（1 条；`small:social-platforms`）→ `社交平台与内容运营`：Juchats 无限爱主题在线聊天与社交平台
- 合并 `社交关系分析`（1 条；`small:social-analysis`）→ `社交平台与内容运营`：Furland 推友关注关系图谱重定向页面
- 合并 `社交媒体开发文档`（1 条；`small:social-media-development`）→ `社交平台与内容运营`：小红书发布服务接口文档与调用说明
- 合并 `社交媒体营销管理`（1 条；`small:social-media-marketing`）→ `社交平台与内容运营`：云略一站式社交媒体营销管理系统
- 合并 `社交媒体运营服务`（1 条；`small:social-media-services`）→ `社交平台与内容运营`：社媒助手联系渠道与社交媒体运营服务咨询
- 合并 `社保与退休金`（2 条；`small:china-social-security`）→ `社交平台与内容运营`：中国城镇职工基本养老保险退休待遇在线测算工具；2024年五险一金社保缴费与到手工资计算器
- 合并 `社保公积金与薪资`（2 条；`small:social-insurance-and-payroll`）→ `社交平台与内容运营`：潮州市五险一金缴纳比例与社保基数查询；深圳市五险一金与税后工资在线计算器

### `03_创作与娱乐 / 内容创作与社交媒体 / 自媒体、博客与创作者工具`（8 个来源目录 / 12 条书签）
- 合并 `AI 写作与内容创作`（1 条；`small:ai-writing-tools`）→ `自媒体、博客与创作者工具`：创作王一站式人工智能内容创作平台登录页
- 合并 `内容创作工具`（2 条；`small:content-creation`）→ `自媒体、博客与创作者工具`：基于 MDX 的微信公众号图文排版在线编辑器；GeekCard：将文本生成精美分享卡片的在线工具
- 合并 `内容创作工具`（1 条；`small:content-creation-tools`）→ `自媒体、博客与创作者工具`：MD2Card Markdown 转换知识卡片在线工具
- 合并 `创作者会员与赞助`（1 条；`small:creator-membership`）→ `自媒体、博客与创作者工具`：爱发电创作者会员订阅与粉丝赞助平台
- 合并 `创作者平台与变现`（2 条；`small:creator-platforms`）→ `自媒体、博客与创作者工具`：爱发电创作者平台新手入门与作品集使用指南；OnlyFans订阅制创作者内容平台首页
- 合并 `博客建站`（1 条；`small:blogging-and-site-building`）→ `自媒体、博客与创作者工具`：彭宏豪创建独立个人博客的建站记录文章
- 合并 `建站与博客系统`（2 条；`small:cms-and-blogging`）→ `自媒体、博客与创作者工具`：Halo开源建站系统与个人博客部署平台；CodingMore网站文章内容后台管理系统
- 合并 `自媒体运营`（2 条；`small:content-operations`）→ `自媒体、博客与创作者工具`：易撰自媒体内容创作与选题辅助工具；鲁搬运营自媒体内容运营服务平台入口

### `03_创作与娱乐 / 游戏、动漫与休闲 / 休闲娱乐与兴趣收藏`（22 个来源目录 / 30 条书签）
- 合并 `云游戏服务`（2 条；`small:cloud-gaming`）→ `休闲娱乐与兴趣收藏`：云玩加CloudPlayPlus云游戏平台官方网站；CloudPlayPlus在线云游戏平台网页应用入口
- 合并 `动漫创意产业机构`（1 条；`small:creative-industry-organizations`）→ `休闲娱乐与兴趣收藏`：青岛市动漫创意产业协会工商企业信息查询
- 合并 `安卓游戏工具`（1 条；`small:android-gaming`）→ `休闲娱乐与兴趣收藏`：GameGuardian 安卓手游数值修改工具 APK 下载说明
- 合并 `小说与电子书检索`（2 条；`small:book-search`）→ `休闲娱乐与兴趣收藏`：BookLink.Me综合在线小说搜索引擎；Z-Library电子书与文献检索数字图书馆
- 合并 `影视娱乐`（1 条；`small:video-entertainment`）→ `休闲娱乐与兴趣收藏`：小宝影院瑞克和莫蒂在线视频搜索结果
- 合并 `怀旧游戏`（2 条；`small:retro-gaming`）→ `休闲娱乐与兴趣收藏`：DOS.lol 浏览器在线运行经典 DOS 游戏与软件平台；老游戏在线玩经典怀旧游戏浏览器运行平台
- 合并 `游戏展会活动`（1 条；`small:gaming-events`）→ `休闲娱乐与兴趣收藏`：2021中国国际数码互动娱乐展览会购票信息
- 合并 `游戏攻略与工具`（1 条；`small:gaming-guides-tools`）→ `休闲娱乐与兴趣收藏`：云顶之弈S17阵容助手与专业阵容推荐工具
- 合并 `游戏文化`（1 条；`small:gaming-culture`）→ `休闲娱乐与兴趣收藏`：MUD游戏玩家花色称谓与身份说明
- 合并 `游戏服务`（2 条；`small:gaming-services`）→ `休闲娱乐与兴趣收藏`：极狐游戏免费网络加速器官方网站；ROMS乐园免费游戏资源与ROM下载网站
- 合并 `游戏硬件外设`（1 条；`small:gaming-hardware`）→ `休闲娱乐与兴趣收藏`：盖世小鸡Gamesir游戏手柄官方产品网站
- 合并 `游戏社区服务`（1 条；`small:gaming-communities`）→ `休闲娱乐与兴趣收藏`：ZERODREAM RACING FiveM 联机竞速服务器
- 合并 `游戏网络加速`（1 条；`small:gaming-network-services`）→ `休闲娱乐与兴趣收藏`：迅游网游加速器官方网络游戏加速服务
- 合并 `游戏网络服务`（2 条；`small:gaming-network`）→ `休闲娱乐与兴趣收藏`：GI免费游戏网络加速器官方网站；游戏加速器品牌推荐与排行榜网站
- 合并 `游戏自动化工具`（1 条；`small:gaming-automation`）→ `休闲娱乐与兴趣收藏`：Docker 自动领取 Epic 免费游戏镜像项目
- 合并 `游戏行业研究`（2 条；`small:gaming-industry-research`）→ `休闲娱乐与兴趣收藏`：DataEye游戏行业数据报告与市场洞察中心；中国音数协游戏工委行业资讯与产业服务网站
- 合并 `游戏视频创作`（1 条；`small:gaming-video-tools`）→ `休闲娱乐与兴趣收藏`：Capturelab游戏内容自动捕捉与短视频制作平台
- 合并 `游戏账号与服务`（2 条；`small:gaming-accounts`）→ `休闲娱乐与兴趣收藏`：英雄联盟北美服Riot游戏账号注册创建页面；Riot Games游戏账号资料与安全设置管理中心
- 合并 `游戏资源`（2 条；`small:gaming-resources`）→ `休闲娱乐与兴趣收藏`：主播同款游戏与相关资源聚合分享页面；王者图鉴：王者荣耀英雄与游戏资料查询站
- 合并 `游戏资讯`（1 条；`small:gaming-news`）→ `休闲娱乐与兴趣收藏`：TouchArcade苹果iPhone与iPad游戏资讯网站
- 合并 `网络小说`（1 条；`small:online-fiction`）→ `休闲娱乐与兴趣收藏`：白糖三两言情小说玉阶遥笼中燕阅读
- 合并 `网络小说阅读`（1 条；`small:web-fiction`）→ `休闲娱乐与兴趣收藏`：Wuxiaworld英文武侠与仙侠网络小说阅读平台

### `03_创作与娱乐 / 游戏、动漫与休闲 / 动漫、漫画与轻小说`（12 个来源目录 / 14 条书签）
- 合并 `二次元视频社区`（1 条；`small:anime-community`）→ `动漫、漫画与轻小说`：AcFun弹幕视频与二次元内容社区首页
- 合并 `动漫与媒体管理`（1 条；`small:anime-and-media`）→ `动漫、漫画与轻小说`：AutoBangumi：自动追番下载与动画媒体库管理项目
- 合并 `动漫图片识别`（2 条；`small:anime-image-search`）→ `动漫、漫画与轻小说`：Ascii2d二次元动漫图片反向来源搜索工具；What Anime Is This动漫截图场景识别引擎
- 合并 `动漫漫画资源下载`（1 条；`small:anime-manga-resources`）→ `动漫、漫画与轻小说`：进击的巨人动画漫画资源下载汇总
- 合并 `动漫识别工具`（1 条；`small:anime-tools`）→ `动漫、漫画与轻小说`：trace.moe以截图反向查询动漫出处的搜索工具
- 合并 `游戏动漫社区`（1 条；`small:gaming-anime`）→ `动漫、漫画与轻小说`：Stage1 萨拉巴第一游戏动漫综合讨论论坛
- 合并 `漫画与娱乐`（1 条；`small:comics-and-entertainment`）→ `动漫、漫画与轻小说`：PicaWeb哔咔漫画网页版在线阅读入口
- 合并 `漫画资源服务`（1 条；`small:manga-resources`）→ `动漫、漫画与轻小说`：集云数据看漫画资源聚合服务网站
- 合并 `电锯人漫画`（2 条；`small:chainsaw-man-manga`）→ `动漫、漫画与轻小说`：电锯人第二部第九十八话汉化讨论页面；少年Jump+官方电锯人第二部第九十八话
- 合并 `网络漫画`（1 条；`small:webcomics`）→ `动漫、漫画与轻小说`：False Knees幽默讽刺四格漫画网站
- 合并 `视觉小说工具资源`（1 条；`small:visual-novel-resources`）→ `动漫、漫画与轻小说`：VNR吧共享目录与视觉小说工具资源索引
- 合并 `轻小说阅读`（1 条；`small:light-novels`）→ `动漫、漫画与轻小说`：《OVERLORD 不死者之王》轻小说在线阅读

### `03_创作与娱乐 / 游戏、动漫与休闲 / 成人内容与敏感站点`（1 个来源目录 / 2 条书签）
- 合并 `成人游戏资源`（2 条；`small:adult-games`）→ `成人内容与敏感站点`：xGames 成人向电子游戏资源平台首页；LewdGames离线成人向二维与三维游戏资源站

### `03_创作与娱乐 / 游戏、动漫与休闲 / 游戏、主机与模组资源`（53 个来源目录 / 70 条书签）
- 合并 `AI模型社区`（1 条；`small:ai-model-communities`）→ `游戏、主机与模组资源`：魔搭社区个人工作台与模型资源概览页面
- 合并 `DNF自动化脚本`（2 条；`small:dnf-automation`）→ `游戏、主机与模组资源`：神迹DNF全自动搬砖倍攻脚本下载页；蓬莱DNF全自动搬砖倍攻脚本下载页
- 合并 `DNF辅助卡密销售`（2 条；`small:dnf-cheat-sales`）→ `游戏、主机与模组资源`：DNF飞机辅助充值卡密销售中心；179发卡网DNF低价提卡商品分类
- 合并 `PC游戏修改器`（2 条；`small:pc-game-trainers`）→ `游戏、主机与模组资源`：FLiNG风灵月影PC游戏修改器与模组官网；布衣爱游戏的单机游戏修改器与升级档资源
- 合并 `PC游戏论坛资源`（1 条；`small:pc-game-forums`）→ `游戏、主机与模组资源`：游民星空论坛PC游戏策略分类资源列表
- 合并 `PC游戏资讯`（1 条；`small:pc-game-news`）→ `游戏、主机与模组资源`：游迅网最新单机游戏资讯新闻频道
- 合并 `Switch主机论坛`（1 条；`small:switch-forums`）→ `游戏、主机与模组资源`：91Wii论坛任天堂Nintendo Switch主机讨论区
- 合并 `Switch使用教程`（1 条；`small:switch-guides`）→ `游戏、主机与模组资源`：Switch大气层使用Awoo安装NSP与XCI游戏教程
- 合并 `Switch金手指资源`（2 条；`small:switch-cheats`）→ `游戏、主机与模组资源`：91Wii论坛Switch金手指代码整合包下载帖；GBAtemp发布的SXOS与Atmosphere金手指代码合集
- 合并 `主机游戏资源`（2 条；`small:console-game-resources`）→ `游戏、主机与模组资源`：游侠论坛PS2与PS1游戏全集资源下载帖；游侠NETSHOW论坛最全TV主机游戏资源版块
- 合并 `主机金手指论坛`（1 条；`small:console-cheat-forums`）→ `游戏、主机与模组资源`：银河战士金手指主机玩家交流论坛首页
- 合并 `乙女游戏资源`（2 条；`small:otome-games`）→ `游戏、主机与模组资源`：乙女游戏蝶之毒华之锁幻想夜话汉化版资源；蝶之毒华之锁汉化版全开存档游戏下载
- 合并 `内容审核与文本过滤`（1 条；`small:content-moderation`）→ `游戏、主机与模组资源`：敏感词过滤检测开源工具使用说明文档
- 合并 `单机游戏下载`（2 条；`small:pc-game-downloads`）→ `游戏、主机与模组资源`：FFF 游戏站的49423单机游戏下载首页；FFF 游戏站的49423单机游戏 VIP 页面
- 合并 `在线游戏与娱乐`（2 条；`small:online-games`）→ `游戏、主机与模组资源`：红色警戒二网页版全平台即时战略游戏；Chess.com 在线与电脑人工智能进行国际象棋对弈
- 合并 `在线生成器与小游戏`（2 条；`small:generators-and-games`）→ `游戏、主机与模组资源`：Maze Generator在线迷宫生成与打印谜题工具；Tikolu在线表情包混合生成与趣味图片制作工具
- 合并 `复古游戏与模拟器资源`（1 条；`small:retro-game-resources`）→ `游戏、主机与模组资源`：老男人游戏网复古主机游戏与模拟器资源
- 合并 `大模型评测与排行`（1 条；`small:ai-model-evaluation`）→ `游戏、主机与模组资源`：LMArena全球人工智能大模型能力排行榜
- 合并 `大模型评测与排行`（1 条；`small:ai-model-reviews`）→ `游戏、主机与模组资源`：林哥大模型野榜主流人工智能模型评测排行
- 合并 `大语言模型`（2 条；`small:llm-models`）→ `游戏、主机与模组资源`：Meta Llama 3 开源大语言模型官方产品介绍页面；OpenRouter上的Llama 3 Soliloquy八十亿参数模型
- 合并 `安卓模拟器`（1 条；`small:android-emulators`）→ `游戏、主机与模组资源`：夜神安卓模拟器电脑版与手游运行平台下载
- 合并 `手机刷机与改装`（1 条；`small:mobile-device-modding`）→ `游戏、主机与模组资源`：华为 Mate 30 Pro 安装操作系统的分步教程
- 合并 `旅行住宿预订`（1 条；`small:travel-accommodation`）→ `游戏、主机与模组资源`：Booking.com缤客酒店民宿与公寓预订平台
- 合并 `时尚与模特`（1 条；`small:fashion-and-models`）→ `游戏、主机与模组资源`：MOKO美空模特与时尚娱乐人才展示平台
- 合并 `模拟器使用教程`（2 条；`small:emulator-guides`）→ `游戏、主机与模组资源`：RPCS3运行战神3的设置参数与演示视频；电玩巴士GBA模拟器下载与使用教程
- 合并 `模拟器金手指资源`（1 条；`small:emulator-cheats`）→ `游戏、主机与模组资源`：EmuSkr多平台模拟器游戏金手指代码资源
- 合并 `游戏下载资源`（2 条；`small:game-download-resources`）→ `游戏、主机与模组资源`：蓝奏云EarlyAccess早期访问资源分享目录；GBT乐赏游戏空间大型免费游戏资源目录
- 合并 `游戏与模组工具`（1 条；`small:games-and-mods`）→ `游戏、主机与模组资源`：太吾绘卷脚本训练器资源文件下载目录
- 合并 `游戏价格与数据查询`（1 条；`small:game-price-tracking`）→ `游戏、主机与模组资源`：SteamDB 游戏价格历史与Steam数据库查询
- 合并 `游戏价格追踪`（1 条；`small:game-prices`）→ `游戏、主机与模组资源`：Lossless Scaling在Steam平台的价格历史
- 合并 `游戏修改讨论`（1 条；`small:game-cheat-discussions`）→ `游戏、主机与模组资源`：Dead In Bermuda反作弊检测与修复讨论
- 合并 `游戏修改资源`（1 条；`small:game-cheat-resources`）→ `游戏、主机与模组资源`：作弊代码网游戏资源分类下载页面
- 合并 `游戏商店账户`（1 条；`small:game-store-accounts`）→ `游戏、主机与模组资源`：Green Man Gaming 游戏商店个人账户资料
- 合并 `游戏工具与社区`（2 条；`small:game-tools-and-communities`）→ `游戏、主机与模组资源`：风灵月影游戏修改器下载与用户交流论坛；Steam游戏账号分享与离线联机资源论坛
- 合并 `游戏推荐与资讯`（1 条；`small:game-discovery`）→ `游戏、主机与模组资源`：游研社玩什么游戏推荐与内容索引
- 合并 `游戏文件工具`（1 条；`small:game-development-tools`）→ `游戏、主机与模组资源`：从英雄联盟游戏文件中提取音频的工具项目
- 合并 `游戏比价工具`（1 条；`small:game-price-tools`）→ `游戏、主机与模组资源`：IsThereAnyDeal 应用授权与账户连接页面
- 合并 `游戏维基资料`（1 条；`small:game-wikis`）→ `游戏、主机与模组资源`：脑叶公司中文维基百科与游戏资料库
- 合并 `游戏美术与设计`（1 条；`small:game-art-and-design`）→ `游戏、主机与模组资源`：TatoomCity韩国游戏设计师Yi Lee个人主页
- 合并 `游戏翻译工具`（1 条；`small:game-translation`）→ `游戏、主机与模组资源`：GTA5游戏聊天专用在线翻译工具
- 合并 `游戏虚拟物品交易`（1 条；`small:game-item-trading`）→ `游戏、主机与模组资源`：DD373游戏账号装备与点卡交易个人中心
- 合并 `游戏账号管理`（2 条；`small:game-accounts`）→ `游戏、主机与模组资源`：Paradox Interactive 账号管理控制台；拳头游戏Riot账号资料与安全管理页面
- 合并 `游戏资料攻略`（1 条；`small:game-guides`）→ `游戏、主机与模组资源`：泰拉瑞亚世界吞噬怪官方中文百科词条
- 合并 `游戏资源下载`（1 条；`small:game-downloads`）→ `游戏、主机与模组资源`：蒸汽游戏宝库Steam单机游戏下载资源站
- 合并 `独立游戏`（1 条；`small:indie-games`）→ `游戏、主机与模组资源`：ANDYLAW.GAMES 独立游戏作品集合
- 合并 `独立游戏资源`（1 条；`small:indie-game-resources`）→ `游戏、主机与模组资源`：Indie Expo今日热门独立网页游戏下载榜单
- 合并 `电子游戏资源`（2 条；`small:game-resources`）→ `游戏、主机与模组资源`：668游戏仓库多平台电子游戏资源中心；Switch520任天堂Switch游戏资源网站
- 合并 `科创教育平台`（1 条；`small:steam-education-platforms`）→ `游戏、主机与模组资源`：枣庄STEAM科创教育云平台课程资源入口
- 合并 `粉丝游戏项目`（1 条；`small:fan-game-projects`）→ `游戏、主机与模组资源`：BIOHAZARD 初代粉丝重制游戏项目页面
- 合并 `红色警戒模组资料`（2 条；`small:red-alert-mods`）→ `游戏、主机与模组资源`：红色警戒2心灵终结盟军单位介绍第七篇；心灵终结3.3在Windows 10兼容运行设置教程
- 合并 `网页游戏`（1 条；`small:web-games`）→ `游戏、主机与模组资源`：拼命玩游戏平台的死亡独轮车 Happy Wheels 页面
- 合并 `音乐节奏网页游戏`（2 条；`small:rhythm-web-games`）→ `游戏、主机与模组资源`：Taiko Web网页版太鼓达人节奏游戏；Melody Muncher Deluxe音乐节奏小游戏
- 合并 `鬼谷八荒模组`（1 条；`small:guigubahuang-mods`）→ `游戏、主机与模组资源`：鬼谷八荒功法一格与内功九格模组制作教程

### `03_创作与娱乐 / 视频、音频与影视 / 媒体平台与观看服务`（6 个来源目录 / 8 条书签）
- 合并 `AI录音与笔记`（2 条；`small:ai-note-tools`）→ `媒体平台与观看服务`：Plaud智能录音设备与语音笔记应用入口；Plaud网页端社区模板与语音笔记资源
- 合并 `动画影视资源`（1 条；`small:animation-resources`）→ `媒体平台与观看服务`：英雄联盟双城之战第二季网盘资源下载页面
- 合并 `媒体视频平台`（1 条；`small:media-platforms`）→ `媒体平台与观看服务`：浙江卫视Z视介平台品牌宣传视频页面
- 合并 `影视媒体库管理`（1 条；`small:media-library`）→ `媒体平台与观看服务`：tinyMediaManager影视媒体库刮削器下载页
- 合并 `影视数据与推荐`（2 条；`small:media-data`）→ `媒体平台与观看服务`：MovieLens电影评分数据集与推荐系统研究平台；South Plus论坛讨论Movie Data Capture影视刮削替代方案
- 合并 `影视资源论坛`（1 条；`small:media-forums`）→ `媒体平台与观看服务`：耐卡影音论坛欧美日韩影视资源交流区

### `03_创作与娱乐 / 视频、音频与影视 / 影视、剧集与字幕资源`（21 个来源目录 / 28 条书签）
- 合并 `剧集下载资源`（1 条；`small:tv-downloads`）→ `影视、剧集与字幕资源`：人人影视剧集下载分享站点
- 合并 `国语影视播放`（1 条；`small:streaming-chinese`）→ `影视、剧集与字幕资源`：9亿好国语配音电影与韩剧在线观看平台
- 合并 `在线影视播放`（1 条；`small:streaming-movies`）→ `影视、剧集与字幕资源`：野荷塘在线影视与伦理影片播放网站
- 合并 `字幕组资源`（2 条；`small:subtitle-groups`）→ `影视、剧集与字幕资源`：电波字幕组美英喜剧字幕与资源主页；SubHD远鉴字幕组主页与作品集合
- 合并 `安卓电视应用`（1 条；`small:android-tv-apps`）→ `影视、剧集与字幕资源`：Emotn Store安卓电视盒子应用商店下载
- 合并 `影视内容`（1 条；`small:film-and-tv`）→ `影视、剧集与字幕资源`：邪教档案之末日风暴影视直播回放页面
- 合并 `影视搜索工具`（1 条；`small:movie-search`）→ `影视、剧集与字幕资源`：茶杯狐电影影视资源搜索与发现平台
- 合并 `影视资源导航`（2 条；`small:film-tv-navigation`）→ `影视、剧集与字幕资源`：549影視森林電影與追劇資源導航站；SuSu分享影音與網路資源聚合網站
- 合并 `影视资源搜索`（1 条；`small:film-tv-search`）→ `影视、剧集与字幕资源`：疯狂影视聚合电影电视剧资源搜索引擎首页
- 合并 `流媒体解锁服务`（1 条；`small:streaming-unlock`）→ `影视、剧集与字幕资源`：老姨子 Netflix 解锁网络服务页面
- 合并 `流媒体订阅服务`（2 条；`small:streaming-services`）→ `影视、剧集与字幕资源`：奈飞Pro流媒体高清套餐购买服务页面；奈飞小铺流媒体影音账号合租服务平台
- 合并 `漫威剧集`（1 条；`small:marvel-series`）→ `影视、剧集与字幕资源`：旺达幻视第六集在线之家播放页面
- 合并 `电影下载资源`（2 条；`small:movie-downloads`）→ `影视、剧集与字幕资源`：武帝电影天堂最新电影与迅雷下载资源；飘花电影网高清电影迅雷下载资源站
- 合并 `电影在线观看资源`（1 条；`small:movie-streaming`）→ `影视、剧集与字幕资源`：动作电影影片列表与在线观看页面
- 合并 `电影推荐`（1 条；`small:movie-recommendations`）→ `影视、剧集与字幕资源`：努努影院高评分科幻电影推荐列表
- 合并 `电影画面与影视参考`（1 条；`small:film-reference`）→ `影视、剧集与字幕资源`：FILMGRAB按导演字母索引浏览电影剧照
- 合并 `电影票房与行业数据`（2 条；`small:film-industry-data`）→ `影视、剧集与字幕资源`：猫眼专业版电影实时票房数据看板；猫眼专业版电影票房市场分析数据平台
- 合并 `直播制作工具`（2 条；`small:live-streaming-tools`）→ `影视、剧集与字幕资源`：OBS插件中心与直播制作插件产品官网；READMEobs直播画面美颜插件下载介绍
- 合并 `直播平台管理`（1 条；`small:live-streaming`）→ `影视、剧集与字幕资源`：斗鱼直播个人中心已关注主播列表
- 合并 `直播行业研究`（1 条；`small:live-streaming-industry`）→ `影视、剧集与字幕资源`：直播观察行业数据与直播生态研究平台
- 合并 `美剧在线观看`（2 条；`small:streaming-american-series`）→ `影视、剧集与字幕资源`：硅谷第一季美剧全集在线观看页面；谎言屋第一季欧美剧全集在线观看

### `03_创作与娱乐 / 视频、音频与影视 / 视频制作、播放器与媒体工具`（17 个来源目录 / 25 条书签）
- 合并 `AI 视频创作`（2 条；`small:ai-video-creation`）→ `视频制作、播放器与媒体工具`：SkyReels 人工智能故事可视化视频生成平台；LibTV面向专业创作者的人工智能视频制作工具
- 合并 `AI 设计与视频生成`（2 条；`small:ai-design-video`）→ `视频制作、播放器与媒体工具`：SVGAnimate AI 生成 SVG 动画与 HTML 视频工具；想得美：基于人工智能的视频生成工具
- 合并 `创意视频与动态视觉`（1 条；`small:video-inspiration`）→ `视频制作、播放器与媒体工具`：开眼Eyepetizer精选短视频发现与观看平台
- 合并 `在线影视资源`（1 条；`small:online-video`）→ `视频制作、播放器与媒体工具`：电影先生在线影视资源与影片播放网站
- 合并 `媒体中心与家庭影音`（2 条；`small:media-centers`）→ `视频制作、播放器与媒体工具`：Kodi 开源家庭影院媒体中心 Windows 版本下载页面；Kodi 无法连接官方插件库错误的排查解决教程
- 合并 `媒体播放器`（2 条；`small:media-players`）→ `视频制作、播放器与媒体工具`：PotPlayer多媒体视频播放器中文语言官网入口；PotPlayer全能影音播放器官方网站与下载入口
- 合并 `影视播放应用`（1 条；`small:video-apps`）→ `视频制作、播放器与媒体工具`：大师兄影视手机全平台视频应用官网
- 合并 `成人影片资源`（1 条；`small:adult-video-resources`）→ `视频制作、播放器与媒体工具`：OneJAV成人影片种子资源索引网站
- 合并 `成人视频下载工具`（1 条；`small:adult-video-tools`）→ `视频制作、播放器与媒体工具`：XXXSave在线视频下载工具中文首页
- 合并 `成人视频工具社区`（1 条；`small:adult-video-community`）→ `视频制作、播放器与媒体工具`：JavPlayer中文交流论坛与使用讨论专区
- 合并 `成人视频网站`（1 条；`small:adult-video-sites`）→ `视频制作、播放器与媒体工具`：MissAV视频网站与成人内容浏览入口
- 合并 `摄影与视频服务`（1 条；`small:photography-video`）→ `视频制作、播放器与媒体工具`：Whitefish蒙大拿州婚礼与活动视频摄影服务
- 合并 `视频与图像处理`（2 条；`small:video-image-tools`）→ `视频制作、播放器与媒体工具`：Unscreen在线自动移除视频背景工具；傲软免费在线视频转换GIF动图制作工具
- 合并 `视频与媒体服务`（2 条；`small:video-and-media`）→ `视频制作、播放器与媒体工具`：movie-web在线视频影视内容聚合播放应用；在线上传字幕文件并自动翻译的字幕处理工具
- 合并 `视频制作素材`（1 条；`small:video-production`）→ `视频制作、播放器与媒体工具`：影视飓风视频制作素材资源页面
- 合并 `视频剪辑工具`（2 条；`small:video-editing`）→ `视频制作、播放器与媒体工具`：AutoCut面向竖屏视频的自动剪辑在线工具；Auto-Editor自动剪辑软件的安装使用文档页面
- 合并 `视频营销工具`（2 条；`small:video-marketing`）→ `视频制作、播放器与媒体工具`：极睿科技iCut智能视频剪辑产品功能介绍页面；极睿科技引流宝短视频营销获客产品官方网站

### `03_创作与娱乐 / 视频、音频与影视 / 音乐、音频与播客`（23 个来源目录 / 31 条书签）
- 合并 `AI 音乐`（1 条；`small:ai-music`）→ `音乐、音频与播客`：Suno AI 生成歌曲夜曲的播放与分享页面
- 合并 `专业音响设备`（1 条；`small:professional-audio`）→ `音乐、音频与播客`：东莞三基音响专业音箱功放产品列表
- 合并 `无损音乐资源社区`（1 条；`small:lossless-music-resources`）→ `音乐、音频与播客`：HiFiNi音乐磁场无损音乐资源社区
- 合并 `订阅与流媒体`（1 条；`small:subscriptions-and-media`）→ `音乐、音频与播客`：通过 Google Play 开通 Spotify Premium 订阅教程
- 合并 `音乐`（1 条；`small:music`）→ `音乐、音频与播客`：安室奈美惠与David Guetta歌曲在线播放
- 合并 `音乐下载工具`（1 条；`small:music-download-tools`）→ `音乐、音频与播客`：MusicTools全网无损音乐下载软件官网
- 合并 `音乐下载资源`（2 条；`small:music-downloads`）→ `音乐、音频与播客`：宝贝DJ音乐网高品质DJ舞曲试听下载；KHInsider电子游戏原声音乐与OST免费下载站
- 合并 `音乐与媒体工具`（1 条；`small:music-and-media`）→ `音乐、音频与播客`：BetterNCM 网易云音乐客户端插件与扩展资源页面
- 合并 `音乐与音频`（2 条；`small:music-and-audio`）→ `音乐、音频与播客`：喜马拉雅听书客户端官方下载与安装入口页面；Spotify歌手Minah单曲作品列表与音乐播放页
- 合并 `音乐创作与配乐`（2 条；`small:music-creation`）→ `音乐、音频与播客`：Epidemic Sound商用免版税音乐与音效曲库；Jukedeck人工智能生成免版税视频配乐工具
- 合并 `音乐学习`（2 条；`small:music-learning`）→ `音乐、音频与播客`：Flowkey在线钢琴课程与曲谱练习平台；Ableton Learning Music免费互动音乐制作课程
- 合并 `音乐搜索与下载`（1 条；`small:music-search`）→ `音乐、音频与播客`：Slider音乐搜索与免费音频下载网站
- 合并 `音乐播放与发现`（1 条；`small:music-listening`）→ `音乐、音频与播客`：Listen 1跨平台歌曲搜索与在线音乐播放器
- 合并 `音乐文化与流派`（1 条；`small:music-culture`）→ `音乐、音频与播客`：Ishkur电子音乐流派发展与知识科普指南
- 合并 `音乐电台与声音探索`（2 条；`small:music-radio`）→ `音乐、音频与播客`：Radio Garden全球广播电台地图实时收听服务；Radio Aporee全球地图环境声音采集档案
- 合并 `音乐节与演唱会`（1 条；`small:music-events`）→ `音乐、音频与播客`：2023 年音乐节与演唱会时间阵容购票汇总
- 合并 `音乐订阅与合租`（2 条；`small:music-subscriptions`）→ `音乐、音频与播客`：悦音坊 Spotify 正版音乐会员合租服务平台；Spotify 合租平台价格、地区与发车渠道对比指南
- 合并 `音乐资源`（2 条；`small:music-resources`）→ `音乐、音频与播客`：MP3BST 免费音乐文件检索目录网站；熊猫无损音乐下载与歌词简谱资源网站
- 合并 `音乐辅助工具`（1 条；`small:music-utilities`）→ `音乐、音频与播客`：网易云音乐歌曲封面图片在线下载助手
- 合并 `音效与音频素材`（1 条；`small:audio-assets`）→ `音乐、音频与播客`：Sonniss GameAudioGDC免费游戏音效资源包
- 合并 `音频与语音服务`（2 条；`small:audio-services`）→ `音乐、音频与播客`：Babel Audio 音频处理与语音服务平台；Babel Audio 音频项目控制台登录入口
- 合并 `音频格式转换工具`（1 条；`small:audio-conversion-tools`）→ `音乐、音频与播客`：网易云音乐NCM格式在线转换为MP3工具
- 合并 `音频编辑工具`（1 条；`small:audio-editing`）→ `音乐、音频与播客`：Audacity 开源音频录制编辑与处理软件下载

### `03_创作与娱乐 / 设计、图片与视觉素材 / 3D 模型、渲染与数字人`（6 个来源目录 / 6 条书签）
- 合并 `3D 模型与素材`（1 条；`small:3d-assets`）→ `3D 模型、渲染与数字人`：飞凡 F7 Rising Auto 2023 汽车三维模型资源
- 合并 `3D角色与数字人`（1 条；`small:3d-character-tools`）→ `3D 模型、渲染与数字人`：Unreal Engine MetaHuman高精度数字人创建平台
- 合并 `三维艺术作品`（1 条；`small:3d-art-inspiration`）→ `3D 模型、渲染与数字人`：The French Monkey超级C4D每日创作项目作品集
- 合并 `三维设计工具`（1 条；`small:3d-design-tools`）→ `3D 模型、渲染与数字人`：Superhuman 在线三维角色创建与人物建模工具
- 合并 `三维设计教程`（1 条；`small:3d-design-tutorials`）→ `3D 模型、渲染与数字人`：C4D灯管字发光文字效果制作课程
- 合并 `人工智能三维工具`（1 条；`small:ai-3d-tools`）→ `3D 模型、渲染与数字人`：Hyper3D Rodin人工智能三维模型生成用户案例集

### `03_创作与娱乐 / 设计、图片与视觉素材 / Adobe、Photoshop 与创作软件`（3 个来源目录 / 4 条书签）
- 合并 `Adobe 软件资源`（2 条；`small:adobe-resources`）→ `Adobe、Photoshop 与创作软件`：Adobe 软件资源下载与安装信息汇总页面；Adobe Creative Cloud 软件版本与补丁资源页面
- 合并 `Photoshop特效素材`（1 条；`small:photoshop-effects`）→ `Adobe、Photoshop 与创作软件`：GraphicRiver三维碎片消散效果Photoshop动作素材
- 合并 `设计插件`（1 条；`small:design-plugins`）→ `Adobe、Photoshop 与创作软件`：PS拉框助手用于Photoshop图表快速制作插件

### `03_创作与娱乐 / 设计、图片与视觉素材 / 图片、摄影与视觉资源`（37 个来源目录 / 49 条书签）
- 合并 `AI图像素材`（1 条；`small:ai-image-assets`）→ `图片、摄影与视觉资源`：Generated Photos人工生成肖像与人物照片素材库
- 合并 `前端可视化开发`（1 条；`small:frontend-visualization-libraries`）→ `图片、摄影与视觉资源`：Apache ECharts开源数据可视化图表库官网
- 合并 `前端数据可视化`（1 条；`small:frontend-visualization`）→ `图片、摄影与视觉资源`：AntV蚂蚁集团数据可视化产品与图表库
- 合并 `动物图片素材`（1 条；`small:animal-images`）→ `图片、摄影与视觉资源`：Cutest Paw全球可爱宠物与动物摄影图片站
- 合并 `历史复古图片`（1 条；`small:historical-photos`）→ `图片、摄影与视觉资源`：New Old Stock旧时代生活影像与黑白历史照片库
- 合并 `可视化与图表工具`（1 条；`small:visualization-tools`）→ `图片、摄影与视觉资源`：Knight Lab 可视化交互时间轴在线制作工具
- 合并 `可视化协作工具`（2 条；`small:visual-collaboration-tools`）→ `图片、摄影与视觉资源`：Bullmind在线流程图绘制与模板推荐工作台；Felt在线地图制作协作与地理数据可视化平台
- 合并 `图像处理与分析`（1 条；`small:image-processing`）→ `图片、摄影与视觉资源`：ImageJ 开源科研图像处理与分析软件平台
- 合并 `图像设计工具`（1 条；`small:image-design-tools`）→ `图片、摄影与视觉资源`：Fermat Toolmaker在线贴纸生成器工具
- 合并 `图片下载工具`（1 条；`small:image-download-tools`）→ `图片、摄影与视觉资源`：PIXID在线浏览与下载Pixiv图片的简易工具
- 合并 `图片与设计素材`（2 条；`small:image-assets`）→ `图片、摄影与视觉资源`：Everypixel多图库商业图片聚合搜索引擎；CartoonStock 商业卡通插画与漫画素材图库
- 合并 `图片拼图与合成`（2 条；`small:image-composition`）→ `图片、摄影与视觉资源`：Icons8 Moose在线场景拼图与人物图片合成工具；美图秀秀网页版模板拼图与海报图片制作工具
- 合并 `图片生成工具`（1 条；`small:image-generators`）→ `图片、摄影与视觉资源`：在线装逼图片生成与趣味制图工具网站
- 合并 `壁纸与图片资源`（1 条；`small:wallpapers-images`）→ `图片、摄影与视觉资源`：Wallpaper Cave高清电脑与手机壁纸图库
- 合并 `复古与公共领域图像`（1 条；`small:vintage-and-archive-images`）→ `图片、摄影与视觉资源`：Viintage公共领域复古图片免费下载资源库
- 合并 `封面图片设计工具`（1 条；`small:cover-image-tools`）→ `图片、摄影与视觉资源`：PicProse 在线文章封面图片生成工具
- 合并 `建筑可视化`（1 条；`small:architectural-visualization`）→ `图片、摄影与视觉资源`：VizPeople建筑可视化三维模型与行业资讯博客
- 合并 `建筑摄影图片`（1 条；`small:architecture-photography`）→ `图片、摄影与视觉资源`：集美大学新图书馆建筑的正版库存照片详情
- 合并 `成人图像与同人内容`（1 条；`small:adult-images`）→ `图片、摄影与视觉资源`：Rule34成人视频与同人图像标签站点
- 合并 `摄影与图像处理`（1 条；`small:photo-editing`）→ `图片、摄影与视觉资源`：darktable 开源 RAW 照片处理与摄影工作流软件
- 合并 `摄影与视觉灵感`（2 条；`small:photo-inspiration`）→ `图片、摄影与视觉资源`：法国PHOTO摄影杂志官方网站与封面作品；Flickr热门标签浏览页：按主题发现摄影图片与作品
- 合并 `摄影作品社区`（1 条；`small:photography-community`）→ `图片、摄影与视觉资源`：500px全球摄影师社区与高质量摄影作品平台
- 合并 `摄影学习与教程`（1 条；`small:photography-learning`）→ `图片、摄影与视觉资源`：Digital Photography School摄影技巧与教程博客
- 合并 `摄影工具与器材`（1 条；`small:photography-tools`）→ `图片、摄影与视觉资源`：Samyang镜头模拟器与摄影焦段视角参考
- 合并 `摄影师作品集`（2 条；`small:photographer-portfolios`）→ `图片、摄影与视觉资源`：摄影师Niklas Alm个人摄影作品展示网站；Anthony Grote体育旅行与生活方式摄影作品
- 合并 `操作系统镜像`（2 条；`small:operating-system-images`）→ `图片、摄影与视觉资源`：Internet Archive 收录的 macOS ISO 系统镜像资源；MediaFire 托管的 macOS Sonoma 系统镜像下载文件
- 合并 `新闻与纪实摄影`（2 条；`small:news-photography`）→ `图片、摄影与视觉资源`：TotallyCoolPix全球实时高清新闻图片分享；《大西洋月刊》新闻纪实摄影与视觉报道图库
- 合并 `气象与地理可视化`（1 条；`small:weather-visualization`）→ `图片、摄影与视觉资源`：earth.nullschool 全球风场天气与海洋状况动态地图
- 合并 `汽车图片与设计`（2 条；`small:automotive-images`）→ `图片、摄影与视觉资源`：NetCarShow全球汽车品牌车型图片与壁纸库；Car UX汽车中控台与车载交互界面设计图库
- 合并 `照片修饰美化`（1 条；`small:photo-retouching`）→ `图片、摄影与视觉资源`：Pho.to在线人像美化修饰与照片润饰工具
- 合并 `照片拼贴与马赛克`（2 条；`small:photo-collage`）→ `图片、摄影与视觉资源`：Shape Collage 自动将照片拼接成形状图案的软件；Picartia免费在线照片马赛克拼贴图片生成器
- 合并 `科学可视化`（2 条；`small:science-visualization`）→ `图片、摄影与视觉资源`：化学元素周期表动态运动可视化页面；Stuff in Space实时查看近地轨道卫星与太空物体
- 合并 `艺术摄影作品`（2 条；`small:art-photography`）→ `图片、摄影与视觉资源`：1x精选高端艺术摄影作品在线图库；1x网站精选高质量艺术摄影作品画廊
- 合并 `表情包与图片素材`（1 条；`small:memes-and-images`）→ `图片、摄影与视觉资源`：AI 技术主题表情包图片素材浏览页面
- 合并 `视觉设计参考`（1 条；`small:visual-reference`）→ `图片、摄影与视觉资源`：IMP Awards历年电影海报收藏与影视视觉参考库
- 合并 `趣味图片生成`（1 条；`small:fun-image-generators`）→ `图片、摄影与视觉资源`：UPUP举牌小人拍立得风格趣味图片在线生成器
- 合并 `链接与图片托管`（2 条；`small:link-and-image-hosting`）→ `图片、摄影与视觉资源`：品云短链接生成与跳转管理服务；品云图床图片上传与外链托管服务

### `03_创作与娱乐 / 设计、图片与视觉素材 / 字体、图标、配色与界面设计`（12 个来源目录 / 16 条书签）
- 合并 `UI设计灵感`（1 条；`small:ui-design-inspiration`）→ `字体、图标、配色与界面设计`：UI Notes真实产品界面设计灵感案例库
- 合并 `UI设计资源`（1 条；`small:ui-design-resources`）→ `字体、图标、配色与界面设计`：Brainwave SaaS创意应用落地页Figma设计套件
- 合并 `产品与用户体验设计`（1 条；`small:product-and-ux-design`）→ `字体、图标、配色与界面设计`：互联网早读课产品设计交互体验与用户研究社区
- 合并 `前端图标资源`（1 条；`small:frontend-icons`）→ `字体、图标、配色与界面设计`：Font Awesome 5图标字体与CSS图标库
- 合并 `图标生成与管理工具`（2 条；`small:icon-tools`）→ `字体、图标、配色与界面设计`：IcoMoon 在线制作SVG图标字体与图标集工具；Icons8桌面应用离线浏览与使用图标素材
- 合并 `字体与图标资源`（2 条；`small:fonts-and-icons`）→ `字体、图标、配色与界面设计`：中国高校校徽字体图标资源库；方正字库阿里健康体字体包下载页面
- 合并 `字体与排版资源`（2 条；`small:fonts`）→ `字体、图标、配色与界面设计`：LXGW 自制开源免费中文字体汇总；朱雀仿宋开源中文正文仿宋字体项目介绍
- 合并 `字体设计社区`（1 条；`small:font-communities`）→ `字体、图标、配色与界面设计`：字体传奇网免费字体下载与中文字体设计交流
- 合并 `字体识别与管理工具`（1 条；`small:font-tools`）→ `字体、图标、配色与界面设计`：Fonts Ninja网页字体识别与字体工作流插件
- 合并 `网页字体服务`（1 条；`small:web-fonts`）→ `字体、图标、配色与界面设计`：有字库中文网页字体WebFont在线服务平台
- 合并 `芯片与科技观点`（1 条；`small:semiconductors-and-tech`）→ `字体、图标、配色与界面设计`：老石谈芯：芯片本质与高效生活方法分享
- 合并 `配色灵感与社区`（2 条；`small:color-inspiration`）→ `字体、图标、配色与界面设计`：配色网色彩搭配方案交流学习与设计灵感社区；Okay Bueno 图片作品配色提取与灵感色板收藏页

### `03_创作与娱乐 / 设计、图片与视觉素材 / 设计灵感与创意资源`（69 个来源目录 / 91 条书签）
- 合并 `AI编程设计规范`（2 条；`small:ai-coding-design-guides`）→ `设计灵感与创意资源`：面向 AI 编程代理的 DESIGN.md 设计规范合集；受 Claude 启发的 DESIGN.md 设计系统规范示例
- 合并 `AI设计与品牌创作`（1 条；`small:ai-design`）→ `设计灵感与创意资源`：Logo-Creator 在线AI品牌标志生成器
- 合并 `AI设计资源`（1 条；`small:ai-design-resources`）→ `设计灵感与创意资源`：MotionSites高品质AI网页首屏设计提示词素材库
- 合并 `API与软件设计`（1 条；`small:api-and-software-design`）→ `设计灵感与创意资源`：DeepL API设计中用于限制滥用的欺骗战术分析
- 合并 `H5页面设计`（1 条；`small:h5-design`）→ `设计灵感与创意资源`：意派Epub360专业H5互动页面在线设计制作平台
- 合并 `OCR 与计算机视觉`（1 条；`small:ocr-and-vision`）→ `设计灵感与创意资源`：PaddleOCR 文字识别框架快速开始开发文档
- 合并 `PPT 模板与素材`（1 条；`small:presentation-resources`）→ `设计灵感与创意资源`：HiPPTer演示文稿模板素材资源导航
- 合并 `中国艺术与文化专题`（2 条；`small:chinese-art`）→ `设计灵感与创意资源`：CGTN 宋代绘画自然主题沉浸式数字专题；CGTN 宋代绘画自然主题中文互动数字专题
- 合并 `书法篆刻资料`（1 条；`small:calligraphy-resources`）→ `设计灵感与创意资源`：书法字典在线书法篆刻字形学习资料平台
- 合并 `交互艺术与实验网页`（2 条；`small:interactive-art`）→ `设计灵感与创意资源`：无限横向滚动的中国山水画沉浸式网页作品；Mikutap可交互音乐视觉效果网页体验
- 合并 `产品展示与样机工具`（2 条；`small:product-design-tools`）→ `设计灵感与创意资源`：Rotato多角度手机设备样机展示制作工具；墨刀移动应用产品原型设计制作教程
- 合并 `产品设计与协作`（1 条；`small:product-design`）→ `设计灵感与创意资源`：网银项目蓝湖产品原型与设计协作页面
- 合并 `但丁与艺术评论`（1 条；`small:dante-art-analysis`）→ `设计灵感与创意资源`：但丁神曲鹰意象与垂直性艺术解读文章
- 合并 `免费图库与素材检索`（2 条；`small:free-stock-assets`）→ `设计灵感与创意资源`：Librestock 免费图片与视频素材聚合搜索引擎；Startup Stock Photos 免费科技办公场景图库
- 合并 `写作与情书素材`（1 条；`small:writing-resources`）→ `设计灵感与创意资源`：手写情书素材资源下载与情书模板网站
- 合并 `创作媒体素材`（1 条；`small:creative-media-assets`）→ `设计灵感与创意资源`：爱给网游戏影视与视频后期素材下载
- 合并 `前端主题与设计`（1 条；`small:frontend-design`）→ `设计灵感与创意资源`：Mix Space 博客系统 Shiro 前端主题展示页面
- 合并 `前端设计与模板灵感`（2 条；`small:frontend-design-inspiration`）→ `设计灵感与创意资源`：NavNav 前端导航栏与 CSS 界面模板灵感库；Resn 创意数字工作室网页交互设计案例作品集
- 合并 `前端设计系统`（1 条；`small:frontend-design-systems`）→ `设计灵感与创意资源`：抖音 Semi Design 企业级中后台设计系统
- 合并 `动态视觉作品`（1 条；`small:motion-design-inspiration`）→ `设计灵感与创意资源`：CUTTT循环动态视觉艺术短片Vimeo播放页面
- 合并 `包装设计`（1 条；`small:packaging-design`）→ `设计灵感与创意资源`：包小盒在线3D包装设计与渲染工具
- 合并 `博物馆与历史`（1 条；`small:museums-history`）→ `设计灵感与创意资源`：埃及文明国家博物馆古埃及木乃伊展览
- 合并 `博物馆与艺术馆藏`（1 条；`small:museums-and-art-collections`）→ `设计灵感与创意资源`：巴黎市立博物馆联盟在线艺术藏品检索平台
- 合并 `品牌素材规范`（2 条；`small:brand-assets`）→ `设计灵感与创意资源`：Meta 官方 Facebook 品牌标志下载与使用规范；Meta 官方 Instagram 品牌素材与视觉使用指南
- 合并 `品牌设计规范`（2 条；`small:design-guidelines`）→ `设计灵感与创意资源`：微信WeDesign品牌设计规范与资源下载页面；全球品牌视觉识别与设计规范文档档案库
- 合并 `在线平面设计工具`（2 条；`small:online-design`）→ `设计灵感与创意资源`：稿定设计：在线制作海报、电商图片与设计模板的工具平台；创客贴在线平面设计与海报模板制作工具
- 合并 `在线绘画工具`（1 条；`small:drawing-tools`）→ `设计灵感与创意资源`：Google AutoDraw人工智能辅助涂鸦绘画工具
- 合并 `在线设计工具`（1 条；`small:online-design-tools`）→ `设计灵感与创意资源`：Crello在线平面设计模板与图片编辑工具
- 合并 `大模型 API 定价`（2 条；`small:llm-api-pricing`）→ `设计灵感与创意资源`：国内外 AI 大语言模型 API 价格对比；Deepbricks AI 模型服务定价页面
- 合并 `大模型 API 文档`（2 条；`small:llm-api-docs`）→ `设计灵感与创意资源`：小米 MiMo 大模型开放平台中文开发文档；阶跃星辰开放平台文本大模型使用文档
- 合并 `大模型平台`（2 条；`small:llm-platforms`）→ `设计灵感与创意资源`：小米 MiMo 人工智能模型产品官网首页；商汤日日新大模型平台 Token 用量套餐页面
- 合并 `大模型硬件参考`（1 条；`small:llm-hardware`）→ `设计灵感与创意资源`：大语言模型推理所需显卡显存容量排行榜
- 合并 `大模型评测`（1 条；`small:llm-evaluation`）→ `设计灵感与创意资源`：SuperCLUE 大模型中文能力评测与排行榜平台
- 合并 `大模型评测榜单`（2 条；`small:llm-leaderboards`）→ `设计灵感与创意资源`：Artificial Analysis 百款大语言模型性能对比排行榜；SuperCLUE 中文大模型能力测评基准与榜单
- 合并 `建站与网页设计`（1 条；`small:website-builders`）→ `设计灵感与创意资源`：Wix免费建站与网页设计平台中文主页
- 合并 `建筑与室内设计`（1 条；`small:architecture-and-interiors`）→ `设计灵感与创意资源`：ArchDaily全球建筑项目案例与设计资讯平台
- 合并 `当代艺术作品`（1 条；`small:contemporary-art`）→ `设计灵感与创意资源`：Ian Fisher持续观察与绘制天空云层的艺术项目
- 合并 `思维模型与决策工具`（1 条；`small:thinking-and-decision-tools`）→ `设计灵感与创意资源`：Untools帮助思考与决策的思维模型工具集合
- 合并 `插画与艺术应用`（1 条；`small:art-and-illustration`）→ `设计灵感与创意资源`：PivisionM Pixiv 第三方安卓客户端介绍与下载
- 合并 `插画创作社区`（2 条；`small:illustration-communities`）→ `设计灵感与创意资源`：drawr手绘插画创作过程与日记交流社区；Pixiv日本插画同人绘画与二次元创作社区
- 合并 `插画师与角色设计作品集`（2 条；`small:illustration-portfolios`）→ `设计灵感与创意资源`：韩国角色艺术家SAKIROO插画设计作品集；Nate Williams国际插画与手写字体作品集
- 合并 `插画约稿`（1 条；`small:illustration-commissions`）→ `设计灵感与创意资源`：米画师商业插画师约稿与作品浏览平台
- 合并 `数字艺术与壁纸`（2 条；`small:digital-art`）→ `设计灵感与创意资源`：CoolVibe数字艺术插画壁纸与灵感画廊；ArtStation全球数字艺术家作品集与招聘平台
- 合并 `数字艺术作品集`（1 条；`small:digital-art-portfolios`）→ `设计灵感与创意资源`：数字插画艺术家Seerlight的SuperRare作品主页
- 合并 `文学与艺术`（1 条；`small:literature-art`）→ `设计灵感与创意资源`：文学中心收录的安吉拉卡特主题同人艺术作品
- 合并 `智慧城市与数字政府`（1 条；`small:smart-city-research`）→ `设计灵感与创意资源`：智慧城市数字政府数字经济与数据要素行业分析平台
- 合并 `本地大模型`（2 条；`small:local-ai`）→ `设计灵感与创意资源`：GPT4All 本地运行开源大语言模型平台；Qwen-7B 通义千问本地部署与 Chatbox 使用教程
- 合并 `沉浸式数字体验`（1 条；`small:immersive-digital-experiences`）→ `设计灵感与创意资源`：太一灵境奇幻世界沉浸式体验官方网站
- 合并 `矢量设计素材`（1 条；`small:vector-design-assets`）→ `设计灵感与创意资源`：懒人图库矢量中国传统风格设计素材专题
- 合并 `私有大模型平台`（1 条；`small:private-llm-platforms`）→ `设计灵感与创意资源`：AI Planet面向个人与企业的私有安全大模型平台
- 合并 `网站设计与搭建`（2 条；`small:web-design-tools`）→ `设计灵感与创意资源`：Webflow 可视化自定义网站设计与构建平台；Pixso AI 原生 UI 设计协作与代码生成工具
- 合并 `网页设计资源`（1 条；`small:web-design-resources`）→ `设计灵感与创意资源`：大学生静态网页设计作业模板资源
- 合并 `艺术交易与画廊`（1 条；`small:art-marketplaces`）→ `设计灵感与创意资源`：Singulart线上画廊与原创当代艺术作品购买平台
- 合并 `艺术创作社区`（1 条；`small:art-communities`）→ `设计灵感与创意资源`：DeviantArt全球插画创作作品展示与艺术社区
- 合并 `艺术品与文物图库`（2 条；`small:art-collections`）→ `设计灵感与创意资源`：中华珍宝馆中国传统艺术品与文物高清图库；芝加哥艺术博物馆公共领域藏品在线检索库
- 合并 `艺术家作品集`（2 条；`small:artist-portfolios`）→ `设计灵感与创意资源`：蒸汽波视觉艺术家Magdiel Lopez个人作品网站；Beeple数字艺术作品与C4D创作资源网站
- 合并 `艺术教育院校`（1 条；`small:art-education`）→ `设计灵感与创意资源`：中央美术学院官方网站与招生艺术资讯
- 合并 `营销页面设计`（1 条；`small:marketing-design`）→ `设计灵感与创意资源`：MAKA免费H5微场景与移动营销页面制作平台
- 合并 `落地页设计与转化工具`（1 条；`small:landing-page-tools`）→ `设计灵感与创意资源`：落地页设计开发与优化工具百项检查清单
- 合并 `虚拟博物馆导览`（1 条；`small:virtual-museums`）→ `设计灵感与创意资源`：毛里茨皇家美术馆线上360度高清虚拟导览
- 合并 `表情包与图片素材`（1 条；`small:meme-resources`）→ `设计灵感与创意资源`：DIY斗图收录的海绵宝宝系列表情包图片合集
- 合并 `设计与创意作品`（1 条；`small:design-and-creative-work`）→ `设计灵感与创意资源`：心乐设计个人站点与创意作品内容展示
- 合并 `设计与原型协作`（2 条；`small:design-collaboration`）→ `设计灵感与创意资源`：产品大牛原型托管与设计标注协作平台；tldraw 开源无限画布与协作白板工具
- 合并 `设计与原型工具`（1 条；`small:design-and-prototyping`）→ `设计灵感与创意资源`：Visily AI 产品原型设计与界面线框图协作工具
- 合并 `设计作品集`（1 条；`small:design-portfolios`）→ `设计灵感与创意资源`：锤子科技设计师Sandor的Dribbble设计作品主页
- 合并 `设计创作社区`（2 条；`small:design-communities`）→ `设计灵感与创意资源`：视觉ME设计师与插画师作品交流社区；UI中国专业用户体验与界面设计交流平台
- 合并 `设计实用工具`（1 条；`small:design-utilities`）→ `设计灵感与创意资源`：Gridzzly自定义网格纸在线生成工具
- 合并 `设计标注与开发交付`（1 条；`small:design-handoff`）→ `设计灵感与创意资源`：马克鳗设计稿尺寸色值标注与测量工具
- 合并 `设计资源导航`（1 条；`small:design-resource-guides`）→ `设计灵感与创意资源`：国外设计素材网站与矢量PSD图标资源导航

### `03_创作与娱乐 / 设计、图片与视觉素材 / 设计特效、模板与素材`（2 个来源目录 / 4 条书签）
- 合并 `反应与视觉测试`（2 条；`small:reaction-tests`）→ `设计特效、模板与素材`：Human Benchmark在线反应时间与速度测试；日文在线动态视力测定与视觉反应测试
- 合并 `故障艺术特效`（2 条；`small:glitch-effects`）→ `设计特效、模板与素材`：PhotoMosh在线故障艺术影像特效制作工具；MOSH在线制作数据故障风格图片与动画工具

### `04_工作与生活 / 办公、效率与知识管理 / 云存储、文件与协作`（1 个来源目录 / 1 条书签）
- 合并 `备份与云存储`（1 条；`small:backup-storage`）→ `云存储、文件与协作`：云朵备份个人数据云端备份与恢复服务

### `04_工作与生活 / 办公、效率与知识管理 / 任务、日程与效率自动化`（4 个来源目录 / 5 条书签）
- 合并 `工作效率工具`（1 条；`small:work-productivity`）→ `任务、日程与效率自动化`：在线自动生成工作周报内容的周报生成器
- 合并 `待办与任务管理`（1 条；`small:task-management`）→ `任务、日程与效率自动化`：TickTick 滴答清单安卓高级版任务管理应用下载
- 合并 `效率与时间管理`（1 条；`small:productivity`）→ `任务、日程与效率自动化`：币安创始人赵长鹏分享个人时间管理法则
- 合并 `效率工具与桌面应用`（2 条；`small:productivity-tools`）→ `任务、日程与效率自动化`：uTools 新一代桌面效率工具平台官方网站；Forest专注计时与效率培养应用网站

### `04_工作与生活 / 办公、效率与知识管理 / 办公效率综合工具`（10 个来源目录 / 12 条书签）
- 合并 `内部项目文档`（1 条；`small:internal-project-docs`）→ `办公效率综合工具`：新国结EFramework项目TAPD文档文件列表
- 合并 `协作工作空间`（1 条；`small:collaboration-workspaces`）→ `办公效率综合工具`：new.space 未命名在线空间页面
- 合并 `在线协作工具`（2 条；`small:online-collaboration`）→ `办公效率综合工具`：Hyperbeam 在线协作浏览器与共同观看应用；Hyperbeam Wandering Shadow 在线共享房间
- 合并 `桌面效率工具`（1 条；`small:desktop-utilities`）→ `办公效率综合工具`：PixPin 截图贴图长截图文字识别工具官网
- 合并 `流程图与协作绘图`（1 条；`small:diagramming-tools`）→ `办公效率综合工具`：ProcessOn免费在线绘图与实时协作平台
- 合并 `笔记本评测`（1 条；`small:laptop-reviews`）→ `办公效率综合工具`：DXOMARK笔记本电脑屏幕性能与产品测评列表
- 合并 `笔记本购买`（2 条；`small:laptop-shopping`）→ `办公效率综合工具`：天猫联想小新 Pro16 2023 锐龙笔记本商品页；Best Buy美国官网MacBook Air M3搜索结果页面
- 合并 `软件工程与协作`（1 条；`small:software-engineering`）→ `办公效率综合工具`：GitHub公司内部培训分享与工程实践资料
- 合并 `邮箱与账户服务`（1 条；`small:email-accounts`）→ `办公效率综合工具`：Yahoo日本邮箱网页版邮件收发界面
- 合并 `邮箱与通信服务`（1 条；`small:email-services`）→ `办公效率综合工具`：Roundcube 网页邮箱收件箱登录页面

### `04_工作与生活 / 办公、效率与知识管理 / 文档、办公与演示材料`（10 个来源目录 / 14 条书签）
- 合并 `PDF 与文档工具`（2 条；`small:pdf-and-documents`）→ `文档、办公与演示材料`：ABBYY 泰比扫描 PDF 文字识别与格式转换软件；Smallpdf 账户个人资料与订阅账单管理页面
- 合并 `写作与文档工具`（1 条；`small:writing-and-document-tools`）→ `文档、办公与演示材料`：Typora所见即所得Markdown写作与阅读编辑器官网
- 合并 `办公与文档工具`（2 条；`small:office-productivity`）→ `文档、办公与演示材料`：煦航毕业论文与Word文档自动排版软件官网；Office Tool Plus微软Office部署管理工具下载
- 合并 `办公协作工具`（1 条；`small:office-and-collaboration`）→ `文档、办公与演示材料`：石墨文档电脑移动端协同办公客户端下载
- 合并 `办公文档模板`（1 条；`small:office-templates`）→ `文档、办公与演示材料`：稻壳儿Docer办公文档简历与演示模板资源
- 合并 `文档处理与解析工具`（2 条；`small:document-tools`）→ `文档、办公与演示材料`：395印章在线生成电子印章与制作印章图片工具；百度文库文档在线解析与内容复制工具
- 合并 `文档处理工具`（1 条；`small:document-processing`）→ `文档、办公与演示材料`：pdf2docx将PDF文档转换为Word的Python文档
- 合并 `文档站与静态站点工具`（1 条；`small:documentation-tools`）→ `文档、办公与演示材料`：VirePressSimple 基于 VitePress 的简洁配置模板
- 合并 `演示文稿分享`（2 条；`small:presentation-sharing`）→ `文档、办公与演示材料`：PPT加行业报告与语音演示内容分享平台；SlideShare 全球专业幻灯片与知识分享平台
- 合并 `纪录片资源`（1 条；`small:documentaries`）→ `文档、办公与演示材料`：IHaveNoTV免费在线纪录片在线观看平台

### `04_工作与生活 / 办公、效率与知识管理 / 笔记、知识管理与书签整理`（12 个来源目录 / 16 条书签）
- 合并 `Notion模板`（2 条；`small:notion-templates`）→ `笔记、知识管理与书签整理`：Notion高级旅行计划模板中文版页面；Easlo的Notion效率模板与数字产品商店
- 合并 `Obsidian资源`（1 条；`small:obsidian-resources`）→ `笔记、知识管理与书签整理`：Obsidian社区2024年度插件与主题宝石获奖名单
- 合并 `个人知识库与笔记`（1 条；`small:personal-knowledge-base`）→ `笔记、知识管理与书签整理`：Wolai协作空间中的aaa001主页面知识库文档
- 合并 `个人记录与知识管理`（1 条；`small:personal-knowledge`）→ `笔记、知识管理与书签整理`：MemoTrace 留痕：个人信息记录与回忆管理工具
- 合并 `博客与知识记录`（1 条；`small:blogs-and-knowledge`）→ `笔记、知识管理与书签整理`：知录网站文章与知识记录内容分类浏览页面
- 合并 `知识查询与计算工具`（1 条；`small:knowledge-tools`）→ `笔记、知识管理与书签整理`：Wolfram Alpha：面向计算、知识查询与数据分析的知识引擎
- 合并 `知识百科与查询`（1 条；`small:knowledge-reference`）→ `笔记、知识管理与书签整理`：互动百科中文百科知识查询平台
- 合并 `知识问答社区`（1 条；`small:knowledge-communities`）→ `笔记、知识管理与书签整理`：Quora 国际知识问答与观点讨论社区
- 合并 `笔记与内容管理`（1 条；`small:notes-and-organization`）→ `笔记、知识管理与书签整理`：xTiles 内容整理笔记管理与项目协作一体化工具
- 合并 `笔记与写作工具`（2 条；`small:notes-and-writing`）→ `笔记、知识管理与书签整理`：我来 Wolai 云端协作与个人笔记客户端下载；幕布 App 多平台大纲笔记客户端下载页面
- 合并 `笔记与知识管理`（2 条；`small:note-taking-tools`）→ `笔记、知识管理与书签整理`：NotePal微信读书笔记导入与整理工具；Montaigne面向苹果设备的笔记应用服务
- 合并 `笔记与知识管理`（2 条；`small:notes-and-knowledge`）→ `笔记、知识管理与书签整理`：Ideanote 在线灵感记录与创意笔记工具；Scrintal 结合笔记管理和思维导图的白板工具

### `04_工作与生活 / 商业、营销与电商 / 商业、产品与行业研究`（13 个来源目录 / 16 条书签）
- 合并 `AI 商业研究与洞察`（1 条；`small:ai-business-research`）→ `商业、产品与行业研究`：BeeBee人工智能商业洞察分析平台
- 合并 `上市公司信息披露`（1 条；`small:listed-company-disclosures`）→ `商业、产品与行业研究`：巨潮资讯网上市公司公告定期报告与信息披露平台
- 合并 `企业与品牌官网`（2 条；`small:business-websites`）→ `商业、产品与行业研究`：言安堂生物科技与健康相关品牌官网；思通数科：信息技术与数据智能服务官网
- 合并 `企业与客户管理软件`（1 条；`small:business-software`）→ `商业、产品与行业研究`：Twenty 开源客户关系管理 CRM 系统官网
- 合并 `企业官网`（2 条；`small:company-websites`）→ `商业、产品与行业研究`：武汉思维跳跃科技有限公司企业官方网站主页；MonTAG品牌或企业官方网站主页入口
- 合并 `企业工商信息`（1 条；`small:business-information`）→ `商业、产品与行业研究`：博联众科（武汉）科技有限公司工商信用信息
- 合并 `企业管理工具`（2 条；`small:business-management-tools`）→ `商业、产品与行业研究`：织蝶云企业OA项目与客户关系管理平台；TinkMail 企业版套餐与服务计划说明文档
- 合并 `公司与团队信息`（1 条；`small:company-profiles`）→ `商业、产品与行业研究`：润去新加坡平台运营公司与团队介绍页面
- 合并 `创业与产品机会`（1 条；`small:startup-resources`）→ `商业、产品与行业研究`：StartZone：发现可盈利创业项目与初创公司机会
- 合并 `创业与企业管理`（1 条；`small:startup-operations`）→ `商业、产品与行业研究`：Captable 开源股权表与公司融资管理平台
- 合并 `商业与客户管理工具`（1 条；`small:business-tools`）→ `商业、产品与行业研究`：客户无忧手机版客户关系管理应用下载
- 合并 `市场规模测算`（1 条；`small:market-sizing`）→ `商业、产品与行业研究`：不同行业潜在市场空间测算方法与分析框架
- 合并 `预测市场`（1 条；`small:prediction-markets`）→ `商业、产品与行业研究`：Polymarket中文站：全球事件预测市场交易平台

### `04_工作与生活 / 商业、营销与电商 / 商业运营与服务平台`（21 个来源目录 / 26 条书签）
- 合并 `企业官网与介绍`（1 条；`small:corporate-profiles`）→ `商业运营与服务平台`：阿拉丁控股集团有限公司官网与企业介绍
- 合并 `企业应用项目`（1 条；`small:enterprise-projects`）→ `商业运营与服务平台`：微人事Java人力资源管理项目文档
- 合并 `企业服务平台`（1 条；`small:enterprise-service-platforms`）→ `商业运营与服务平台`：鹏云网络企业服务平台主界面
- 合并 `企业榜单与指数`（1 条；`small:rankings-and-indices`）→ `商业运营与服务平台`：胡润研究院中国财富榜单与行业指数报告
- 合并 `企业科技行业报告`（1 条；`small:enterprise-tech-reports`）→ `商业运营与服务平台`：T媒体企业数字化与科技行业研究报告库
- 合并 `信息技术企业`（2 条；`small:technology-companies`）→ `商业运营与服务平台`：统信国基官方网站与信息技术服务介绍；智科集团上海广东成都山东业务与机构介绍
- 合并 `医疗科技企业`（1 条；`small:healthcare-companies`）→ `商业运营与服务平台`：贝医科技医疗健康技术服务官方网站
- 合并 `咨询与商业洞察`（2 条；`small:consulting-insights`）→ `商业运营与服务平台`：普华永道中国行业洞察与专业服务研究报告；麦肯锡中国洞见文章与麦肯锡季刊研究内容
- 合并 `外勤销售管理`（1 条；`small:sales-field-management`）→ `商业运营与服务平台`：聘聘云拜访通外勤拜访管理系统入口
- 合并 `客户关系管理工具`（1 条；`small:crm-tools`）→ `商业运营与服务平台`：Cordys开源AI CRM客户关系管理系统与私有化部署
- 合并 `客服与帮助台工具`（1 条；`small:customer-support-tools`）→ `商业运营与服务平台`：Intercom 面向 AI 智能体时代的客服帮助台平台
- 合并 `工业企业信息`（2 条；`small:industrial-companies`）→ `商业运营与服务平台`：河南液压气动公司简介与企业信息页面；北重集团官方网站与重型装备企业信息
- 合并 `房产运营工具`（2 条；`small:real-estate-tools`）→ `商业运营与服务平台`：推房神器房源采集群发与刷新工具登录页面；链家厦门商业贷款与公积金房贷计算器工具
- 合并 `房地产企业信息`（1 条；`small:real-estate-companies`）→ `商业运营与服务平台`：保利地产投资顾问有限公司企业介绍页面
- 合并 `新媒体运营`（2 条；`small:new-media-operations`）→ `商业运营与服务平台`：新榜内容创业账号运营与新媒体数据服务平台；考拉新媒体导航收录运营营销工具与行业资源
- 合并 `直播电商与 MCN`（1 条；`small:live-commerce`）→ `商业运营与服务平台`：珀箫文化直播带货 MCN 与政务媒体直播服务
- 合并 `直播电商企业`（1 条；`small:live-commerce-companies`）→ `商业运营与服务平台`：遥望科技直播电商与新媒体营销业务官网
- 合并 `科技新闻与创业资讯`（1 条；`small:tech-news`）→ `商业运营与服务平台`：Hacker News科技创业新闻讨论社区
- 合并 `腕表品牌资料`（1 条；`small:watch-reference`）→ `商业运营与服务平台`：腕表之家全球男女手表品牌标志与资料大全
- 合并 `财经商业资讯`（1 条；`small:financial-news`）→ `商业运营与服务平台`：21经济网中国财经新闻与商业经济资讯门户
- 合并 `邮件客户端`（1 条；`small:email-tools`）→ `商业运营与服务平台`：Foxmail Windows版邮件客户端官方下载

### `04_工作与生活 / 商业、营销与电商 / 电商、商品与供应链`（10 个来源目录 / 11 条书签）
- 合并 `产品增长与营销`（1 条；`small:product-growth`）→ `电商、商品与供应链`：Pushkeen.AI：浏览优秀应用推送通知与增长案例
- 合并 `产品管理与协作工具`（1 条；`small:product-management-tools`）→ `电商、商品与供应链`：Polyscore面向产品开发团队的智能评估与协作平台
- 合并 `产品管理学习资源`（1 条；`small:product-management-learning`）→ `电商、商品与供应链`：Lenny产品经理通讯订阅会员与学习资源通行证
- 合并 `创业与产品增长`（1 条；`small:startup-product`）→ `电商、商品与供应链`：Instagram早期创业历程与创始人特质分析
- 合并 `品牌与选购指南`（1 条；`small:shopping-guides`）→ `电商、商品与供应链`：买购网品牌排行与消费知识指南
- 合并 `商品热销排行`（1 条；`small:shopping-rankings`）→ `电商、商品与供应链`：拼多多数码品类今日热销商品排行榜数据页面
- 合并 `批发采购货源`（1 条；`small:wholesale-sourcing`）→ `电商、商品与供应链`：阿里巴巴1688批发采购与货源交易平台
- 合并 `电商营销工具`（1 条；`small:ecommerce-marketing`）→ `电商、商品与供应链`：天猫云码营销引擎账号登录与营销管理入口
- 合并 `电商零售产业资讯`（2 条；`small:ecommerce-industry`）→ `电商、商品与供应链`：亿邦动力电商产业资讯研究与商业观察平台；星图数据电商消费热点与零售市场洞察平台
- 合并 `综合电商购物`（1 条；`small:general-shopping`）→ `电商、商品与供应链`：天猫综合零售购物与品牌商品交易平台

### `04_工作与生活 / 商业、营销与电商 / 营销、广告与品牌增长`（11 个来源目录 / 16 条书签）
- 合并 `SEO 与网站运营`（1 条；`small:seo-and-webmaster`）→ `营销、广告与品牌增长`：桔子SEO网站长搜索引擎优化经验分享平台
- 合并 `SEO与站长工具`（2 条；`small:seo-webmaster`）→ `营销、广告与品牌增长`：Bing Webmaster Tools站长工具介绍；Bing站长工具网站所有权验证页面
- 合并 `SEO与网站运营`（1 条；`small:seo-marketing`）→ `营销、广告与品牌增长`：Ahrefs面向初学者的搜索引擎优化完整指南
- 合并 `内容营销案例`（1 条；`small:content-marketing`）→ `营销、广告与品牌增长`：剧星传媒视频内容整合营销案例与服务展示
- 合并 `医药健康品牌`（1 条；`small:health-brands`）→ `营销、广告与品牌增长`：三浦制药日本健康医药企业官方网站
- 合并 `品牌网站案例`（2 条；`small:brand-websites`）→ `营销、广告与品牌增长`：Enod 法国餐饮品牌展示网站与视觉案例；LINDBERG 高端丹麦眼镜品牌中国官方网站
- 合并 `广告营销案例`（1 条；`small:marketing-cases`）→ `营销、广告与品牌增长`：数英网品牌广告营销项目与创意案例库
- 合并 `美容护肤品牌`（2 条；`small:beauty-brands`）→ `营销、广告与品牌增长`：碧妍思BIOYONCE品牌产品介绍页面；德玛贝尔美容护肤品牌的官方网站与品牌介绍
- 合并 `营销工具与服务`（2 条；`small:marketing-tools-and-services`）→ `营销、广告与品牌增长`：鱼爪传媒网络推广与社交视频营销服务平台；addog广告营销从业者常用工具与资源导航
- 合并 `营销推广工具`（2 条；`small:marketing-tools`）→ `营销、广告与品牌增长`：私域引流宝开源推广工具控制台首页；私域引流宝开源推广工具登录页面
- 合并 `营销资讯与案例`（1 条；`small:marketing-news-and-cases`）→ `营销、广告与品牌增长`：梅花网营销传播资讯与品牌营销案例数据库

### `04_工作与生活 / 生活、出行与消费 / 住房、家居与车辆`（4 个来源目录 / 4 条书签）
- 合并 `汽车选购与预订`（1 条；`small:car-shopping`）→ `住房、家居与车辆`：梅赛德斯奔驰车型意向金在线预订订单页面
- 合并 `深圳租房住房`（1 条；`small:shenzhen-housing`）→ `住房、家居与车辆`：深圳万科泊寓公寓房源与整租出租查询
- 合并 `租房与房源搜索`（1 条；`small:housing-search`）→ `住房、家居与车辆`：杭州多平台地图找房租房房源聚合检索引擎
- 合并 `租房与房源搜索`（1 条；`small:rentals-housing`）→ `住房、家居与车辆`：自如上海地图找房与租房房源搜索页面

### `04_工作与生活 / 生活、出行与消费 / 健康、家庭与兴趣生活`（7 个来源目录 / 10 条书签）
- 合并 `健康与护肤产品`（2 条；`small:health-and-skincare`）→ `健康、家庭与兴趣生活`：Romega鲱鱼卵Omega-3营养补充产品介绍；My Green Herbs修丽可护肤产品系列商城
- 合并 `健康医疗服务`（1 条；`small:health-services`）→ `健康、家庭与兴趣生活`：厦门市民健康管理个人服务平台入口
- 合并 `医疗健康信息`（1 条；`small:health-information`）→ `健康、家庭与兴趣生活`：国家权威中国医药信息与药品疾病查询平台
- 合并 `医疗服务`（2 条；`small:healthcare-services`）→ `健康、家庭与兴趣生活`：四川大学华西医院官方网站首页入口；北京协和医院官方网站首页入口
- 合并 `医疗科技资讯`（1 条；`small:health-tech`）→ `健康、家庭与兴趣生活`：中山医大携手微软培育智慧医疗人才新闻报道
- 合并 `美食与烹饪`（1 条；`small:food-recipes`）→ `健康、家庭与兴趣生活`：Allrecipes全球家常菜谱与烹饪灵感网站
- 合并 `计算机技能竞赛`（2 条；`small:computer-competitions`）→ `健康、家庭与兴趣生活`：2023年第六届大学生计算机技能应用大赛官网；大学生计算机技能应用大赛决赛入围名单公示

### `04_工作与生活 / 生活、出行与消费 / 旅行、通信与出行服务`（5 个来源目录 / 5 条书签）
- 合并 `地图与世界观生成`（1 条；`small:map-generators`）→ `旅行、通信与出行服务`：Azgaar奇幻世界地图随机生成器
- 合并 `天气与地图`（1 条；`small:weather-and-maps`）→ `旅行、通信与出行服务`：Windy全球风力地图与天气预报查询平台
- 合并 `旅行与旅居服务`（1 条；`small:travel-and-living`）→ `旅行、通信与出行服务`：明日故乡中国城市旅居数据筛选查询工具
- 合并 `旅行攻略社区`（1 条；`small:travel-guides`）→ `旅行、通信与出行服务`：穷游网自由行攻略与旅行经验分享社区
- 合并 `虚拟旅行与环境体验`（1 条；`small:virtual-travel`）→ `旅行、通信与出行服务`：Drive and Listen：边开车边聆听世界城市声音

### `04_工作与生活 / 生活、出行与消费 / 日常生活与消费参考`（19 个来源目录 / 25 条书签）
- 合并 `成人交友与社交`（2 条；`small:adult-dating`）→ `日常生活与消费参考`：Kamii开放式关系成人交友平台首页；名媛圈全国线下约会交友平台入口
- 合并 `成人夜生活信息`（1 条；`small:adult-nightlife-info`）→ `日常生活与消费参考`：香港夜生活成人服务信息与资料目录网站
- 合并 `搜索与地图导航`（2 条；`small:search-and-navigation`）→ `日常生活与消费参考`：Google香港中文版网页搜索入口；高德地图在线地图与路线导航服务
- 合并 `日常实用小工具`（2 条；`small:daily-life-tools`）→ `日常生活与消费参考`：911查询人民币金额数字转中文大写工具；中国亲戚关系称谓计算器与家族关系查询
- 合并 `日本生活与迁居`（1 条；`small:japan-living`）→ `日常生活与消费参考`：东京都面向外国居民的教育支持与入学生活指南
- 合并 `时尚与生活方式`（2 条；`small:fashion-and-lifestyle`）→ `日常生活与消费参考`：时尚网高端白领生活方式与潮流资讯平台；东京Style Arena日本街头时尚穿搭观察网站
- 合并 `本地生活社区`（1 条；`small:local-communities`）→ `日常生活与消费参考`：洛杉矶华人本地资讯分类信息交流平台
- 合并 `汽车改装工具`（1 条；`small:auto-tools`）→ `日常生活与消费参考`：3DTuning在线三维汽车改装与车型定制工具
- 合并 `海外生活与迁居`（2 条；`small:overseas-living`）→ `日常生活与消费参考`：赴新加坡落地后三个月安顿生活实用指南；个人海外迁居与肉身翻墙过程记录上篇
- 合并 `深圳生活指南`（1 条；`small:shenzhen-living`）→ `日常生活与消费参考`：深圳各区图书馆地址服务与借阅攻略汇总
- 合并 `物流快递服务`（2 条；`small:logistics`）→ `日常生活与消费参考`：顺丰速运运单SF3156001161130物流查询；快递100快递查询寄件与物流管理平台
- 合并 `生活成本与城市比较`（1 条；`small:cost-of-living`）→ `日常生活与消费参考`：Numbeo全球城市生活成本比较计算工具
- 合并 `生活技巧指南`（1 条；`small:life-guides`）→ `日常生活与消费参考`：中文wikiHow生活技能与实用指南首页
- 合并 `生活技能与操作指南`（1 条；`small:how-to-guides`）→ `日常生活与消费参考`：wikiHow生活技能与操作方法图文指南
- 合并 `生活方式与个人成长`（1 条；`small:lifestyle-growth`）→ `日常生活与消费参考`：Zen Habits极简生活习惯与个人成长博客
- 合并 `程序化生成与地图`（1 条；`small:procedural-generation`）→ `日常生活与消费参考`：Watabou Procgen Arcana 程序化地图生成器合集
- 合并 `美国生活与城市`（1 条；`small:us-living`）→ `日常生活与消费参考`：MoveMap按生活偏好筛选比较美国宜居城市工具
- 合并 `营养与健康`（1 条；`small:nutrition`）→ `日常生活与消费参考`：FatSecret中国食物热量与营养成分查询
- 合并 `香港生活与证件`（1 条；`small:hong-kong-living`）→ `日常生活与消费参考`：内地驾照免试换领香港十年正式驾照办理攻略

### `04_工作与生活 / 生活、出行与消费 / 购物、订阅与生活服务`（17 个来源目录 / 24 条书签）
- 合并 `Apple订单管理`（1 条；`small:apple-account-orders`）→ `购物、订阅与生活服务`：Apple中国大陆商城订单详情查询页面
- 合并 `Spotify 订阅与开通`（2 条；`small:spotify-subscriptions`）→ `购物、订阅与生活服务`：小猫 Music 巴西区 Spotify 高级会员订阅商品页；Spotify 菲律宾区订阅开通与充值使用教程
- 合并 `商城与购物`（1 条；`small:shopping`）→ `购物、订阅与生活服务`：老K自营店 商品分类与自营资源商城
- 合并 `在线购物`（1 条；`small:online-shopping`）→ `购物、订阅与生活服务`：亚马逊中国在线购物商城官方网站首页
- 合并 `成人用品购物`（1 条；`small:adult-shopping`）→ `购物、订阅与生活服务`：花语人形 XT DOLL 硅胶仿真人偶产品商城
- 合并 `本地生活与账户服务`（2 条；`small:local-services`）→ `购物、订阅与生活服务`：宁波市自来水有限公司官方网站与供水服务；58同城个人用户中心与账户管理入口
- 合并 `本地生活服务`（1 条；`small:local-life-services`）→ `购物、订阅与生活服务`：大众点评杭州美食生活团购与旅游服务
- 合并 `汽车服务`（2 条；`small:auto-services`）→ `购物、订阅与生活服务`：车质网汽车质量投诉处理与车型口碑评价平台；瓜子二手车移动端车辆买卖与估价服务平台
- 合并 `海外订阅服务指南`（2 条；`small:subscription-guides`）→ `购物、订阅与生活服务`：土耳其区 iCloud 服务订阅与上车操作指南；Microsoft Office 365 A 系列订阅与激活说明
- 合并 `海外购物攻略`（2 条；`small:overseas-shopping-guides`）→ `购物、订阅与生活服务`：2023年美国亚马逊海淘MacBook购买攻略；20个美国百货海淘网站与一站式购物指南
- 合并 `消费品评测与排行`（2 条；`small:consumer-product-reviews`）→ `购物、订阅与生活服务`：盖得排行消费品品牌与产品口碑榜单查询；口碑平替高性价比商品替代品收录与推荐平台
- 合并 `消费电子选购`（1 条；`small:electronics-shopping`）→ `购物、订阅与生活服务`：一加10 Pro万物青12GB加256GB产品页
- 合并 `眼镜与隐形眼镜购物`（1 条；`small:eyewear-shopping`）→ `购物、订阅与生活服务`：可得眼镜网上配镜隐形眼镜及美瞳购物商城
- 合并 `购物与比价工具`（1 条；`small:shopping-tools`）→ `购物、订阅与生活服务`：最佳平替：通过低价替代搜索词寻找高性价比商品
- 合并 `购物优惠与折扣`（2 条；`small:shopping-deals`）→ `购物、订阅与生活服务`：白菜坑优惠券特价商品与限量抢购信息平台；大忽悠宠粉活动商品商城与 2023 年合集页面
- 合并 `软件订阅服务评测`（1 条；`small:software-subscriptions`）→ `购物、订阅与生活服务`：爱范儿评测电脑软件订阅服务与使用价值
- 合并 `邮政快递服务`（1 条；`small:postal-services`）→ `购物、订阅与生活服务`：国家邮政局申诉服务个人投诉查询页面

### `04_工作与生活 / 网站、社区与待整理 / 个人站点、博客与社区`（8 个来源目录 / 11 条书签）
- 合并 `个人博客`（2 条；`small:personal-blogs`）→ `个人站点、博客与社区`：守望轩记录生活思考与个人随笔博客；CVcat 梦想编织屋个人博客文章目录
- 合并 `个人网站主页`（2 条；`small:personal-websites`）→ `个人站点、博客与社区`：刘佳珺个人网站与在线主页入口；师爷的康城个人社区与娱乐网站首页
- 合并 `个人页面与社区`（1 条；`small:personal-communities`）→ `个人站点、博客与社区`：捡个便宜个人资源分享与交友页面
- 合并 `博客与网站管理`（1 条；`small:blog-administration`）→ `个人站点、博客与社区`：内网Halo博客系统管理仪表板
- 合并 `社区与平台`（1 条；`small:community-platforms`）→ `个人站点、博客与社区`：河马小镇线上平台与社区服务首页
- 合并 `社群与资源共享`（2 条；`small:community-resources`）→ `个人站点、博客与社区`：Teambition Thoughts 资源群欢迎与共享页面；Telegram 简体中文热门频道前一百名榜单
- 合并 `论坛分类信息`（1 条；`small:forum-classifieds`）→ `个人站点、博客与社区`：买可乐论坛普通可乐分类版块页面
- 合并 `逆向与破解技术论坛`（1 条；`small:reverse-engineering-forums`）→ `个人站点、博客与社区`：随心逆向破解论坛的软件与游戏技术交流区

### `04_工作与生活 / 网站、社区与待整理 / 在线服务与管理入口`（28 个来源目录 / 35 条书签）
- 合并 `临时号码与短信服务`（2 条；`small:temporary-number-services`）→ `在线服务与管理入口`：国内外在线短信接码平台资源合集；无敌接码在线短信接收与临时号码平台
- 合并 `临时号码与短信服务`（1 条；`small:temporary-phone-services`）→ `在线服务与管理入口`：SMS-Man 临时虚拟手机号与短信验证码服务
- 合并 `产品与服务官网`（1 条；`small:product-services`）→ `在线服务与管理入口`：Airy 官网产品与服务信息页面
- 合并 `众包与微任务平台`（2 条；`small:microtask-platforms`）→ `在线服务与管理入口`：Clickworker在线微任务众包兼职赚钱平台；UHRS众包微任务在线标注与任务交易市场入口
- 合并 `其他服务站点`（1 条；`small:misc-services`）→ `在线服务与管理入口`：Full Motion网站首页与服务内容入口
- 合并 `在线平台`（1 条；`small:online-platforms`）→ `在线服务与管理入口`：WeView 在线网站与内容浏览服务首页
- 合并 `在线服务控制台`（2 条；`small:service-dashboards`）→ `在线服务与管理入口`：闪电服务后台控制台仪表盘；Lunabot AI 平台账户登录与控制台入口
- 合并 `媒体服务账户`（1 条；`small:media-service-accounts`）→ `在线服务与管理入口`：Infinite Titan媒体服务器用户中心
- 合并 `工信人才服务`（1 条；`small:industry-talent-services`）→ `在线服务与管理入口`：工业和信息化部人才交流中心官方网站首页
- 合并 `待确认在线服务`（2 条；`small:uncategorized-services`）→ `在线服务与管理入口`：Light2F网站服务与功能入口页面；Vposy在线服务网站主页与功能入口
- 合并 `志愿服务`（1 条；`small:volunteer-service`）→ `在线服务与管理入口`：志愿厦门志愿服务用户个人中心
- 合并 `担保交易服务`（1 条；`small:transaction-services`）→ `在线服务与管理入口`：新币担保官网服务入口与平台信息页面
- 合并 `支付订阅服务`（1 条；`small:payment-services`）→ `在线服务与管理入口`：野卡海外软件订阅支付与虚拟卡服务
- 合并 `本地管理入口`（1 条；`small:local-admin`）→ `在线服务与管理入口`：ZDH系统主页与本地服务控制入口
- 合并 `本地管理工具`（1 条；`small:local-admin-tools`）→ `在线服务与管理入口`：本地交易记录管理系统的事务列表页面
- 合并 `票务与抢票服务`（1 条；`small:ticketing-services`）→ `在线服务与管理入口`：Bypass分流抢票与购票辅助服务平台
- 合并 `管理后台项目`（1 条；`small:admin-dashboard-projects`）→ `在线服务与管理入口`：Gin-Vue-Admin前后端分离管理后台开源项目
- 合并 `网站与网络服务`（1 条；`small:web-services`）→ `在线服务与管理入口`：鱼鸢网络网站建设与互联网技术服务官方网站
- 合并 `网站后台管理`（2 条；`small:website-admin`）→ `在线服务与管理入口`：刘佳珺网站后台插件管理控制台；极睿科技CMS后台首页管理平台
- 合并 `网站管理与备案`（1 条；`small:website-administration`）→ `在线服务与管理入口`：工信部ICP域名与IP地址备案信息管理系统
- 合并 `聊天与社交服务`（1 条；`small:chat-services`）→ `在线服务与管理入口`：RawChat公益聊天服务与共享站点
- 合并 `苹果官方服务`（1 条；`small:apple-services`）→ `在线服务与管理入口`：苹果中国大陆Apple Store零售店地址列表查询
- 合并 `虚拟号码与短信服务`（1 条；`small:virtual-number-services`）→ `在线服务与管理入口`：EOMSG 在线虚拟手机号短信验证码接收平台
- 合并 `虚拟服务平台`（2 条；`small:virtual-services`）→ `在线服务与管理入口`：光速代充自助下单与虚拟服务充值平台；光速代刷自助下单与账号增值服务平台
- 合并 `证券账户与交易工具`（2 条；`small:securities-account-tools`）→ `在线服务与管理入口`：富途牛牛账户登录与美股客户端下载入口；富途牛牛国内账户设置与个人配置页面
- 合并 `账号与验证服务`（1 条；`small:online-accounts`）→ `在线服务与管理入口`：5SIM 在线接收短信验证码与虚拟号码服务
- 合并 `账户与订阅管理`（1 条；`small:account-management`）→ `在线服务与管理入口`：大圣净化星空辽阔账号系统用户管理面板
- 合并 `链接跳转服务`（1 条；`small:link-services`）→ `在线服务与管理入口`：vlink 短链接跳转与浏览器打开提示页面

### `04_工作与生活 / 网站、社区与待整理 / 待整理与跨主题收藏`（138 个来源目录 / 169 条书签）
- 合并 `ASU校园账户与工具`（2 条；`small:asu-tools`）→ `待整理与跨主题收藏`：亚利桑那州立大学 Zoom 个人账户资料设置；亚利桑那州立大学 Dropbox 免费云盘文件入口
- 合并 `GIF动图与动画`（1 条；`small:gif-and-animation`）→ `待整理与跨主题收藏`：GIPHY动态GIF动图搜索制作与分享平台
- 合并 `ROM修改工具`（1 条；`small:rom-hacking-tools`）→ `待整理与跨主题收藏`：Romhacking.net游戏ROM修改工具下载页面
- 合并 `Telegram 使用与资源`（1 条；`small:telegram`）→ `待整理与跨主题收藏`：Telegram贴纸制作及已有表情包本地下载教程
- 合并 `Web3与元宇宙`（1 条；`small:web3-and-metaverse`）→ `待整理与跨主题收藏`：元宇宙系列报告：Web3.0开启互联网新阶段
- 合并 `Web性能测试`（1 条；`small:web-performance`）→ `待整理与跨主题收藏`：Speedometer 3 浏览器网页应用性能基准测试详情
- 合并 `专利与知识产权`（1 条；`small:patents-intellectual-property`）→ `待整理与跨主题收藏`：世界知识产权组织 WIPO 国际专利与知识产权检索
- 合并 `专利与知识产权查询`（1 条；`small:patent-and-ip`）→ `待整理与跨主题收藏`：中国国家知识产权局专利公布公告查询系统
- 合并 `东盟国别资料`（2 条；`small:asean-country-profiles`）→ `待整理与跨主题收藏`：中国一带一路网文莱国家概况与合作信息页面；中国一带一路网印度尼西亚国家详情与合作动态
- 合并 `书影音资料整理`（1 条；`small:media-catalogs`）→ `待整理与跨主题收藏`：豆瓣未收录书籍影视音乐条目汇总
- 合并 `互联网工具导航`（2 条；`small:tool-directories`）→ `待整理与跨主题收藏`：阿猫阿狗互联网产品运营设计工具导航平台；创造狮设计师与互联网从业者工具资源导航
- 合并 `交通工具与车型`（1 条；`small:vehicle-products`）→ `待整理与跨主题收藏`：张雪机车820RR摩托车产品参数与车型展示
- 合并 `产业园区项目`（1 条；`small:industrial-park-projects`）→ `待整理与跨主题收藏`：鼎祥时代日照基地项目与园区运营信息
- 合并 `产品参数与性能对比`（1 条；`small:product-comparisons`）→ `待整理与跨主题收藏`：Versus万物参数规格与性能在线对比平台
- 合并 `产品反馈与需求管理`（1 条；`small:product-feedback`）→ `待整理与跨主题收藏`：Canny 用户反馈收集与产品功能投票管理平台
- 合并 `产品发现与社区`（2 条；`small:product-discovery`）→ `待整理与跨主题收藏`：Product Hunt 最新科技产品与应用发现社区；一网一匠极速版新产品发现与推荐社区
- 合并 `产品样机工具`（1 条；`small:mockup-tools`）→ `待整理与跨主题收藏`：Smartmockups在线产品展示样机快速生成平台
- 合并 `人物资料与访谈`（2 条；`small:people-profiles`）→ `待整理与跨主题收藏`：CapRadio电台工作人员Chris Hagan个人简介；摄影师Lucia Litman个人简介与创作背景
- 合并 `健身训练`（1 条；`small:fitness-training`）→ `待整理与跨主题收藏`：MuscleWiki按身体部位查询训练动作
- 合并 `共青团组织服务`（1 条；`small:youth-organization`）→ `待整理与跨主题收藏`：网上共青团智慧团建系统组织关系与团员管理平台
- 合并 `内容编辑与排版`（1 条；`small:content-authoring`）→ `待整理与跨主题收藏`：基于 MDX 的微信图文排版在线编辑器
- 合并 `写作与文本校验`（1 条；`small:writing-and-text-tools`）→ `待整理与跨主题收藏`：zhlint中文文案格式规范与排版错误校验工具
- 合并 `创意互动网页工具`（1 条；`small:creative-web-tools`）→ `待整理与跨主题收藏`：Neave Interactive 创意网页互动与在线小工具合集
- 合并 `创意服务平台`（1 条；`small:creative-marketplaces`）→ `待整理与跨主题收藏`：画加插画师约稿与商业美术交易平台
- 合并 `创意生成工具`（1 条；`small:creative-generators`）→ `待整理与跨主题收藏`：RAKKOTOOLS 文本与图片 ASCII 字符画生成器
- 合并 `剪贴板工具`（1 条；`small:clipboard-tools`）→ `待整理与跨主题收藏`：剪切助手提升复制粘贴效率的桌面工具
- 合并 `加密货币与NFT`（2 条；`small:crypto-nft`）→ `待整理与跨主题收藏`：OpenSea上的CLONE X村上隆潮流头像NFT合集；VestLab加密代币上线与分发信息查询平台
- 合并 `加密资产交易`（2 条；`small:crypto-trading`）→ `待整理与跨主题收藏`：币安加密货币现货与衍生品交易平台；OKX 加密货币交易平台与比特币行情入口
- 合并 `动物保护公益`（1 条；`small:animal-welfare`）→ `待整理与跨主题收藏`：中国小动物保护协会官方网站与公益资讯
- 合并 `动画与特效生成工具`（1 条；`small:animation-generators`）→ `待整理与跨主题收藏`：日式液体特效动画GIF在线生成工具页面
- 合并 `单位换算工具`（2 条；`small:unit-converters`）→ `待整理与跨主题收藏`：转换器网万能在线单位换算与转换工具；转换器网在线长度单位换算工具
- 合并 `占位内容工具`（1 条；`small:placeholder-tools`）→ `待整理与跨主题收藏`：CanCMS中文随机假文与页面占位内容生成器
- 合并 `压缩与归档工具`（1 条；`small:archive-tools`）→ `待整理与跨主题收藏`：WinRAR Windows 压缩解压软件官方下载与支持页面
- 合并 `名字查询与取名`（1 条；`small:naming-tools`）→ `待整理与跨主题收藏`：Behind the Name外国名字含义历史与随机取名网站
- 合并 `命令行与终端工具`（2 条；`small:command-line-tools`）→ `待整理与跨主题收藏`：x-cmd 轻量级集成终端命令行工具官网；X-CMD 管理开发工具与扩展命令的脚本框架
- 合并 `命理与占卜工具`（1 条；`small:divination-tools`）→ `待整理与跨主题收藏`：Tianfu Agent 紫微斗数八字奇门遁甲命理推演引擎
- 合并 `图示与流程图工具`（1 条；`small:diagram-tools`）→ `待整理与跨主题收藏`：亿图图示 EdrawMax 流程图与图表设计软件
- 合并 `在线实用工具`（1 条；`small:general-online-tools`）→ `待整理与跨主题收藏`：OneKeyTools在线实用工具集合与其他工具页面
- 合并 `在线工具集`（1 条；`small:web-tools`）→ `待整理与跨主题收藏`：颜夕网络网站的在线工具集页面
- 合并 `屏幕录制与演示制作`（1 条；`small:screen-recording`）→ `待整理与跨主题收藏`：FocuSee自动缩放镜头效果的屏幕录制工具
- 合并 `展会与招商信息`（1 条；`small:exhibitions-events`）→ `待整理与跨主题收藏`：E展网国内展会招商信息与会展查询平台
- 合并 `应用发布工具`（1 条；`small:app-publishing`）→ `待整理与跨主题收藏`：摩秀软件应用商店截图与展示图在线生成工具
- 合并 `录屏与摄像头工具`（2 条；`small:screen-recording-and-camera`）→ `待整理与跨主题收藏`：oCam电脑屏幕录制软件官方下载页面；iVCam将手机用作电脑摄像头的软件工具
- 合并 `影像设备评测`（1 条；`small:camera-reviews`）→ `待整理与跨主题收藏`：DXOMARK相机影像质量测试评分与评测
- 合并 `待整理项目`（1 条；`small:unknown-projects`）→ `待整理与跨主题收藏`：Aalab Found实验室或创意项目主页
- 合并 `待确认技术资源`（1 条；`small:uncategorized-tech`）→ `待整理与跨主题收藏`：SNPS技术站点CJContent内容页面
- 合并 `待确认网站`（1 条；`small:uncategorized`）→ `待整理与跨主题收藏`：tingdong.cn：听懂相关服务与内容网站入口
- 合并 `待确认网站`（1 条；`small:uncategorized-sites`）→ `待整理与跨主题收藏`：01sun匿名网站首页与2025页面入口
- 合并 `待识别在线应用`（1 条；`small:unclassified-web-apps`）→ `待整理与跨主题收藏`：VBR 在线应用访问入口
- 合并 `性能测试工具`（1 条；`small:performance-testing`）→ `待整理与跨主题收藏`：oha 命令行 HTTP 压力测试与负载生成工具
- 合并 `技术写作网站项目`（2 条；`small:tech-writing-projects`）→ `待整理与跨主题收藏`：TechScribes 技术内容网站首页演示页面；TechScribes 技术写作网站项目展示主页
- 合并 `技术文章与案例`（1 条；`small:technology-articles`）→ `待整理与跨主题收藏`：Cloudflare账户疑似因异常流量被暂停的经历与警示
- 合并 `技术社区与组织`（1 条；`small:tech-communities`）→ `待整理与跨主题收藏`：亚利桑那州立大学 ASU Hacking Club 社团主页
- 合并 `技术讨论与社区`（1 条；`small:technology-discussions`）→ `待整理与跨主题收藏`：Hacker News关于低代码软件开发真实性的讨论
- 合并 `政治与时事新闻`（1 条；`small:politics-news`）→ `待整理与跨主题收藏`：彭博图解习近平崛起与中国权力集中的历程
- 合并 `文本处理工具`（1 条；`small:text-processing`）→ `待整理与跨主题收藏`：WordsAway 在线文本处理与文本防检测工具
- 合并 `新闻媒体`（1 条；`small:news-media`）→ `待整理与跨主题收藏`：纽约时报中文网国际新闻与深度报道首页
- 合并 `新闻媒体机构`（1 条；`small:news-and-media`）→ `待整理与跨主题收藏`：新华社机构基本情况与官方信息介绍页面
- 合并 `新闻评论与观点`（1 条；`small:news-commentary`）→ `待整理与跨主题收藏`：凤凰网凤凰评论频道时事观点文章平台
- 合并 `新闻调查报道`（1 条；`small:news-investigations`）→ `待整理与跨主题收藏`：BBC中文调查性侵影片网站幕后主谋报道
- 合并 `日剧指南`（1 条；`small:japanese-drama-guides`）→ `待整理与跨主题收藏`：91日剧寻找好看日剧与追新剧指南
- 合并 `日常实用工具`（1 条；`small:daily-tools`）→ `待整理与跨主题收藏`：手机在线亲戚称呼关系计算器工具
- 合并 `日常实用工具`（1 条；`small:utility-tools`）→ `待整理与跨主题收藏`：蛙蛙工具在线人民币金额大写转换器
- 合并 `时间与时区工具`（1 条；`small:time-and-timezone-tools`）→ `待整理与跨主题收藏`：Every Time Zone全球时差换算与会议时间工具
- 合并 `智能驾驶产业资讯`（1 条；`small:autonomous-driving-news`）→ `待整理与跨主题收藏`：飞凡智驾首席科学家离职与上汽业务整合报道
- 合并 `本科教学质量报告`（2 条；`small:teaching-quality-reports`）→ `待整理与跨主题收藏`：金陵科技学院2022至2023学年本科教学质量报告；广州航海学院2022至2023年度本科教学质量报告
- 合并 `极客设备与桌搭`（1 条；`small:tech-setups`）→ `待整理与跨主题收藏`：Geekerwan RoomTour 极客玩家房间设备展示
- 合并 `桌面美化工具`（2 条；`small:desktop-customization`）→ `待整理与跨主题收藏`：Rainmeter Windows桌面美化与系统监控组件工具；米哈游人工桌面鹿鸣动态壁纸官方页面
- 合并 `模拟工具`（1 条；`small:simulation-tools`）→ `待整理与跨主题收藏`：NUKEMAP核爆炸影响范围在线模拟地图工具
- 合并 `正则表达式工具与规则`（2 条；`small:regex-tools`）→ `待整理与跨主题收藏`：Any-Rule 常用正则表达式规则大全；常用正则表达式在线生成与测试工具
- 合并 `活动创意与案例灵感`（1 条；`small:creative-inspiration`）→ `待整理与跨主题收藏`：黑猫会网易主题活动创意案例图片搜索结果
- 合并 `消息平台与机器人`（1 条；`small:messaging-and-bots`）→ `待整理与跨主题收藏`：Telegram 机器人目录与评分排行平台
- 合并 `演讲与思想分享`（1 条；`small:talks-ideas`）→ `待整理与跨主题收藏`：TED 全球思想演讲与知识传播平台
- 合并 `演讲与沟通表达`（1 条；`small:communication-skills`）→ `待整理与跨主题收藏`：Gary Yang 杨天颖演讲表达与商务呈现教练资源
- 合并 `烟草产品信息`（1 条；`small:tobacco-information`）→ `待整理与跨主题收藏`：烟悦网利群香烟产品资料与信息搜索结果
- 合并 `热点榜单聚合`（1 条；`small:news-aggregators`）→ `待整理与跨主题收藏`：今日热榜聚合各平台实时热点榜单
- 合并 `爱死亡和机器人`（2 条；`small:love-death-robots`）→ `待整理与跨主题收藏`：爱死亡和机器人第二季字幕组资源页；爱死亡和机器人动画剧集视频页面
- 合并 `犬种与犬展服务`（2 条；`small:dog-breeds-and-shows`）→ `待整理与跨主题收藏`：中国犬业协会 CKU 官方犬种赛事服务网站；CKU 犬种百科中的萨摩耶犬品种资料
- 合并 `犬舍与繁育信息`（1 条；`small:dog-breeding`）→ `待整理与跨主题收藏`：摩王家族萨摩耶犬舍繁育犬只信息页面
- 合并 `生态保护项目`（1 条；`small:conservation-projects`）→ `待整理与跨主题收藏`：全球一百种失落物种保护专题项目
- 合并 `用户认证与权限`（1 条；`small:authentication`）→ `待整理与跨主题收藏`：SuperTokens 开源用户认证与会话管理方案
- 合并 `电动车资讯与报价`（1 条；`small:electric-vehicles`）→ `待整理与跨主题收藏`：全球电动车网电动车与电动汽车报价资讯平台
- 合并 `电子制造与PCB`（1 条；`small:electronics-manufacturing`）→ `待整理与跨主题收藏`：嘉立创国产PCB线路板CAM软件产品官网
- 合并 `界面模拟工具`（1 条；`small:ui-mockups`）→ `待整理与跨主题收藏`：在线健康码界面模拟工具
- 合并 `短信测试工具`（2 条；`small:sms-testing`）→ `待整理与跨主题收藏`：蓝奏云分享的短信压力测试软件安装包；在线短信压力测试服务网站入口页面
- 合并 `禅修与心灵文字`（1 条；`small:meditation`）→ `待整理与跨主题收藏`：内酷禅心关于清静寂定心境的文字站
- 合并 `移动应用与安装包`（1 条；`small:mobile-apps`）→ `待整理与跨主题收藏`：抖音iOS应用30.3.0版IPA安装包下载
- 合并 `移动设备管理工具`（2 条；`small:mobile-device-tools`）→ `待整理与跨主题收藏`：爱思助手苹果设备刷机越狱与管理工具官网；CzDEV搞机工具箱第三方文件分享下载页面
- 合并 `简历写作指南`（1 条；`small:resume-guides`）→ `待整理与跨主题收藏`：简单简历基础内容模块填写与撰写指南
- 合并 `算法与刷题学习`（2 条；`small:algorithm-learning`）→ `待整理与跨主题收藏`：代码随想录算法刷题路线与数据结构教程；LeetCode 动态规划刷题 Notion 学习模板
- 合并 `红色警戒工具`（2 条；`small:red-alert-tools`）→ `待整理与跨主题收藏`：我要玩游戏网站与红警修改工具入口；我要玩游戏红色警戒修改大师入口
- 合并 `绝命毒师`（2 条；`small:breaking-bad`）→ `待整理与跨主题收藏`：绝命毒师第五季第九集在线播放页面；绝命毒师第三季第一集剧迷播放页
- 合并 `综合在线工具`（1 条；`small:online-utilities`）→ `待整理与跨主题收藏`：随便吧综合在线生成查询与网名设计工具集
- 合并 `综合新闻资讯`（2 条；`small:general-news`）→ `待整理与跨主题收藏`：界面新闻重点时政财经商业新闻平台；凤凰网综合新闻资讯与媒体门户网站
- 合并 `网站导航目录`（1 条；`small:web-directories`）→ `待整理与跨主题收藏`：a667a 网站导航分类页面
- 合并 `网站性能测试`（1 条；`small:web-performance-tools`）→ `待整理与跨主题收藏`：WebPageTest 对 xwww.online 的网站性能测试结果
- 合并 `网站模板资源`（1 条；`small:website-templates`）→ `待整理与跨主题收藏`：Envato Elements 的 Elementor 网站模板套件资源库
- 合并 `网络与通用工具`（1 条；`small:web-utilities`）→ `待整理与跨主题收藏`：DuckDuckGo注重隐私保护的互联网搜索引擎首页
- 合并 `网络直播`（1 条；`small:live-tv`）→ `待整理与跨主题收藏`：直播大全在线直播频道地区分类页
- 合并 `网页WebGL特效`（1 条；`small:webgl-web-effects`）→ `待整理与跨主题收藏`：Codrops Infinite Lights炫酷流光网页特效演示
- 合并 `脑力训练与测试`（2 条；`small:cognitive-training`）→ `待整理与跨主题收藏`：Lumosity认知能力训练与益智游戏平台；Human Benchmark 在线认知能力与反应速度测试平台
- 合并 `自托管应用`（1 条；`small:self-hosted-apps`）→ `待整理与跨主题收藏`：Immich自托管照片与视频管理项目官网
- 合并 `航班追踪查询`（2 条；`small:flight-tracking`）→ `待整理与跨主题收藏`：厦门航空MF8593航班2023年3月历史飞行记录；FlightAware实时航班状态与飞行轨迹查询
- 合并 `航空与航班追踪`（1 条；`small:aviation-tracking`）→ `待整理与跨主题收藏`：FlightAware航班追踪服务账户管理页面
- 合并 `英雄联盟手游辅助`（1 条；`small:lol-mobile-assistance`）→ `待整理与跨主题收藏`：壬申工作室提供的英雄联盟手游外挂网站
- 合并 `药品监管查询`（2 条；`small:drug-regulation`）→ `待整理与跨主题收藏`：国家药品监督管理局药品医疗器械数据查询；国家药监局化妆品备案注册数据查询入口
- 合并 `虚拟主播动态`（1 条；`small:virtual-streamers`）→ `待整理与跨主题收藏`：VTBs in bilibili虚拟主播动态聚合
- 合并 `表单与问卷工具`（1 条；`small:form-tools`）→ `待整理与跨主题收藏`：TDUCK 填鸭表单：在线表单收集与数据管理平台
- 合并 `观赏鱼饲养`（2 条；`small:ornamental-fish`）→ `待整理与跨主题收藏`：龙鱼之巅关于粗线银版龙鱼的讨论帖子；地图鱼品种介绍与观赏鱼百科资料
- 合并 `设备互联工具`（1 条；`small:device-connectivity`）→ `待整理与跨主题收藏`：OPPO一加设备跨屏互联官方下载入口
- 合并 `设备管理与工具`（2 条；`small:device-tools`）→ `待整理与跨主题收藏`：Karl's Drive中的联想设备相关工具正式版资源；联想LegionZone游戏设备官方服务与管理平台首页
- 合并 `设备维修与拆解指南`（1 条；`small:repair-guides`）→ `待整理与跨主题收藏`：iFixit中文站：免费电子设备维修拆解手册
- 合并 `跑步与运动记录`（1 条；`small:fitness-and-running`）→ `待整理与跨主题收藏`：Running Page个人跑步记录与运动数据主页
- 合并 `路由器评测`（1 条；`small:router-reviews`）→ `待整理与跨主题收藏`：中国移动RAX3000Q路由器拆机评测交流
- 合并 `车辆信息查询`（1 条；`small:vehicle-lookups`）→ `待整理与跨主题收藏`：驾照网全国车辆车牌号归属地在线查询
- 合并 `车辆公告与法规`（1 条；`small:vehicle-regulations`）→ `待整理与跨主题收藏`：工信部第333批道路机动车生产企业及产品公告变更名单
- 合并 `软件商城与账户`（2 条；`small:software-stores`）→ `待整理与跨主题收藏`：数码荔枝软件商店个人账号设置管理页面；数码荔枝正版软件商城与优秀应用推荐首页
- 合并 `软件安装与使用`（1 条；`small:software-installation`）→ `待整理与跨主题收藏`：TORCS 赛车模拟器源代码安装与运行说明
- 合并 `软件密钥与激活资源`（1 条；`small:software-keys`）→ `待整理与跨主题收藏`：爱好网共享密钥资源与激活信息发布页面
- 合并 `软件架构`（1 条；`small:software-architecture`）→ `待整理与跨主题收藏`：微服务架构设计模式视频课程购买与学习页面
- 合并 `软件资源社区`（1 条；`small:software-communities`）→ `待整理与跨主题收藏`：Mobilism移动应用资源与论坛社区首页
- 合并 `软件项目与扩展`（2 条；`small:software-projects`）→ `待整理与跨主题收藏`：Microblock 软件插件与开源项目资源聚合主页；Bitbucket开源项目letsgogo_21代码仓库说明文档
- 合并 `追星与活动记录`（1 条；`small:fandom-and-events`）→ `待整理与跨主题收藏`：推活日记：记录偶像应援活动与线下演出行程
- 合并 `通信与号码工具`（1 条；`small:telecom-tools`）→ `待整理与跨主题收藏`：FreeCarrierLookup美国手机号码运营商免费查询工具
- 合并 `通用实用工具`（1 条；`small:general-utility-tools`）→ `待整理与跨主题收藏`：CC趣味屋在线二维码生成与二维码解码工具
- 合并 `通用查询工具`（1 条；`small:general-query-tools`）→ `待整理与跨主题收藏`：IP138地址归属地与号码信息在线查询
- 合并 `通知与消息推送`（1 条；`small:notification-tools`）→ `待整理与跨主题收藏`：WePush多渠道消息批量推送工具与配置说明
- 合并 `配置管理与中间件`（1 条；`small:configuration-management`）→ `待整理与跨主题收藏`：Apollo分布式配置中心项目中文使用文档首页
- 合并 `链接检测工具`（1 条；`small:link-tools`）→ `待整理与跨主题收藏`：WhatsLink陌生网址跳转目标安全查询工具
- 合并 `零售与即时配送`（1 条；`small:retail-delivery`）→ `待整理与跨主题收藏`：同城酒库酒类零售与即时配送服务官网
- 合并 `静态站点与博客工具`（1 条；`small:static-site-tools`）→ `待整理与跨主题收藏`：Hugo 高性能静态网站生成器官方网站
- 合并 `风险工具归档`（1 条；`small:risky-tools`）→ `待整理与跨主题收藏`：在线短信轰炸工具及骚扰服务页面
- 合并 `高性能计算与EDA`（2 条；`small:hpc-and-eda`）→ `待整理与跨主题收藏`：速石科技Fsched与SLF集群调度器介绍；速石科技EDA仿真与云计算案例资料中心
- 合并 `高性能计算与集群`（1 条；`small:hpc-and-clusters`）→ `待整理与跨主题收藏`：Slurm高性能计算任务调度器官方文档
- 合并 `高端珠宝产品`（1 条；`small:luxury-jewelry`）→ `待整理与跨主题收藏`：海瑞温斯顿Emerald系列18K白金珠宝产品页
- 合并 `高级腕表`（1 条；`small:luxury-watches`）→ `待整理与跨主题收藏`：江诗丹顿官方全系列高级腕表选购页面
- 合并 `高风险与滥用内容`（1 条；`small:unsafe-content`）→ `待整理与跨主题收藏`：在线短信轰炸服务页面
- 合并 `鬼谷八荒修改工具`（2 条；`small:guigubahuang-tools`）→ `待整理与跨主题收藏`：鬼谷八荒主宰修改器2.3.1下载页面；鬼谷八荒ToGameMod内置修改器更新讨论帖

### `04_工作与生活 / 网站、社区与待整理 / 成人内容与敏感站点`（3 个来源目录 / 4 条书签）
- 合并 `成人社区讨论`（1 条；`small:adult-communities`）→ `成人内容与敏感站点`：141华人社区香港成人话题讨论专区
- 合并 `成人网站推荐与评测`（2 条；`small:adult-site-guides`）→ `成人内容与敏感站点`：Porn Dude成人陪侍网站推荐目录；Porn Dude中文色情交友网站推荐与评测
- 合并 `成人话题讨论`（1 条；`small:adult-discussions`）→ `成人内容与敏感站点`：PTT网友分享一天内多次射精的个人经验讨论

### `04_工作与生活 / 网站、社区与待整理 / 网站导航与资源入口`（7 个来源目录 / 8 条书签）
- 合并 `Telegram 资源与工具`（1 条；`small:telegram-resources`）→ `网站导航与资源入口`：Telegram机器人资源整理与常见功能介绍文章
- 合并 `免费资源导航`（1 条；`small:free-resources`）→ `网站导航与资源入口`：FMHY免费媒体软件与网络资源导航索引
- 合并 `管理后台与登录`（1 条；`small:admin-portals`）→ `网站导航与资源入口`：SherryYun网站后台管理系统登录入口
- 合并 `网址导航`（1 条；`small:site-navigation`）→ `网站导航与资源入口`：I社中国相关网址发布导航页面
- 合并 `美剧资源`（2 条；`small:american-series-resources`）→ `网站导航与资源入口`：美剧虫蓝光美剧影视资源聚合网站；爱美剧美剧在线观看与下载资源网站
- 合并 `苹果软件资源`（1 条；`small:apple-software-resources`）→ `网站导航与资源入口`：沧海苹果应用站热门苹果软件资源平台首页
- 合并 `账户与平台入口`（1 条；`small:account-portals`）→ `网站导航与资源入口`：Outlier Community 账户偏好设置页面

### `04_工作与生活 / 职业、政务与金融 / 招聘、求职与职业发展`（16 个来源目录 / 21 条书签）
- 合并 `兼职与招聘信息`（1 条；`small:job-listings`）→ `招聘、求职与职业发展`：Fiwfans泰语兼职服务信息聚合平台
- 合并 `创意与技术职位`（1 条；`small:creative-job-boards`）→ `招聘、求职与职业发展`：Authentic Jobs设计创意与开发职位招聘板
- 合并 `实习与职位招聘`（2 条；`small:internships-and-jobs`）→ `招聘、求职与职业发展`：厦门人才网本地招聘求职与人力资源服务平台；实习僧全国实习岗位搜索与招聘服务平台
- 合并 `招聘与求职`（1 条；`small:jobs`）→ `招聘、求职与职业发展`：Gaggle Net公司公开招聘职位与求职申请页面
- 合并 `招聘管理工具`（1 条；`small:recruitment-tools`）→ `招聘、求职与职业发展`：智联招聘企业用户快速认证与账号开通页面
- 合并 `求职与技术面试`（1 条；`small:career-and-interviews`）→ `招聘、求职与职业发展`：技术面试八股文知识点与求职备考资料页面
- 合并 `求职与职业工具`（1 条；`small:career-tools`）→ `招聘、求职与职业发展`：老鱼简历在线创建编辑与展示个人求职简历页面
- 合并 `求职与职业机会`（1 条；`small:jobs-and-careers`）→ `招聘、求职与职业发展`：华盛顿特区远程转录专员职位申请页面
- 合并 `求职经验与职业发展`（1 条；`small:career-guides`）→ `招聘、求职与职业发展`：李辉分享五年待业后的求职经历文章
- 合并 `求职面试准备`（1 条；`small:job-interview`）→ `招聘、求职与职业发展`：行为事件访谈与胜任力面试自我介绍准备指南
- 合并 `海外职业与移民`（2 条；`small:overseas-career`）→ `招聘、求职与职业发展`：德国技术人才移民法与工作居留政策说明；TechPays Europe 欧洲科技行业程序员薪资查询平台
- 合并 `科技职业与薪资`（2 条；`small:tech-careers`）→ `招聘、求职与职业发展`：2022年全球各国程序员薪资水平与收入差距报告；2022年各国软件工程师平均薪资数据比较报告
- 合并 `综合求职与招聘平台`（2 条；`small:general-job-boards`）→ `招聘、求职与职业发展`：Monster综合职位搜索职业建议与招聘资源平台；CareerBuilder全球地区招聘站点入口页面
- 合并 `职业与行业观察`（1 条；`small:career-and-industry`）→ `招聘、求职与职业发展`：金融行业程序员工作职责与职业体验访谈文章
- 合并 `金融行业求职`（2 条；`small:finance-careers`）→ `招聘、求职与职业发展`：AlphaSights中文客户服务专员2024招聘职位；职业梦垂直金融行业招聘与职业培训平台
- 合并 `零售行业求职`（1 条；`small:retail-careers`）→ `招聘、求职与职业发展`：优衣库中国招聘网申系统与职位申请入口

### `04_工作与生活 / 职业、政务与金融 / 政务、公共服务与政策`（15 个来源目录 / 22 条书签）
- 合并 `个人金融服务`（1 条；`small:personal-finance-services`）→ `政务、公共服务与政策`：中国人民银行征信中心个人信用查询
- 合并 `人社政务服务`（2 条；`small:human-resources-services`）→ `政务、公共服务与政策`：国家12333人力资源和社会保障在线服务目录；江苏领航人力资源服务集团官方网站
- 合并 `企业政务服务`（2 条；`small:business-government`）→ `政务、公共服务与政策`：浙江省企业登记全程电子化在线办理平台；杭州亲清在线政企服务与惠企政策平台
- 合并 `住房保障服务`（1 条；`small:housing-services`）→ `政务、公共服务与政策`：深圳人才安居住房服务官方网站
- 合并 `公共服务`（2 条；`small:public-services`）→ `政务、公共服务与政策`：通信大数据行程卡官方登录与行程查询服务入口；互联网信息服务投诉平台我的投诉查询页
- 合并 `厦门人才政策`（1 条；`small:xiamen-talent-policy`）→ `政务、公共服务与政策`：厦门市博士人才落户政策公共服务说明
- 合并 `广东政务服务`（1 条；`small:guangdong-public-services`）→ `政务、公共服务与政策`：广东省人力资源和社会保障厅网上服务平台
- 合并 `政务与公共服务`（2 条；`small:government-public-services`）→ `政务、公共服务与政策`：黑龙江省中医药相关官方网站首页；国家技能人才评价证书全国联网查询系统
- 合并 `政府政策与规划`（1 条；`small:government-policy`）→ `政务、公共服务与政策`：厦门市十四五生态文明建设规划实施通知
- 合并 `政府采购服务`（1 条；`small:government-procurement`）→ `政务、公共服务与政策`：江西省政府采购电子卖场供应商管理入口
- 合并 `标准与政策`（2 条；`small:standards-and-policy`）→ `政务、公共服务与政策`：全国标准信息公共服务平台标准检索首页；GB/T 36073-2018 数据管理能力成熟度评估模型
- 合并 `海关政务服务`（1 条；`small:customs-government`）→ `政务、公共服务与政策`：中华人民共和国海关总署政务服务与海关政策信息官网
- 合并 `深圳公共服务`（1 条；`small:shenzhen-public-services`）→ `政务、公共服务与政策`：深圳金融社保卡申领办理指南与更新信息
- 合并 `自由职业与外包服务`（2 条；`small:freelance-services`）→ `政务、公共服务与政策`：Fiverr全球自由职业音视频定制服务交易平台；猪八戒网企业服务与自由职业项目交易平台
- 合并 `金融账户与支付`（2 条；`small:financial-services`）→ `政务、公共服务与政策`：Fiat24数字金融账户个人仪表盘；PayPal美国在线支付转账与资金管理首页

### `04_工作与生活 / 职业、政务与金融 / 职业与社会服务资源`（15 个来源目录 / 17 条书签）
- 合并 `人才政策服务`（1 条；`small:talent-policies`）→ `职业与社会服务资源`：杭州市人才政策详情与申报服务页面
- 合并 `厦门落户政策`（1 条；`small:xiamen-residency`）→ `职业与社会服务资源`：厦门人才引进与落户政策办理信息
- 合并 `国际职业技能赛事`（1 条；`small:international-skills-competitions`）→ `职业与社会服务资源`：一带一路暨金砖国家技能发展技术创新大赛官网
- 合并 `投资与基金`（1 条；`small:investing`）→ `职业与社会服务资源`：丁忆童Rookie Fund基金会员服务页面
- 合并 `投资交流社区`（1 条；`small:investing-communities`）→ `职业与社会服务资源`：雪球中国股票投资者交流社区与行情资讯平台
- 合并 `深圳落户政策`（2 条；`small:shenzhen-residency`）→ `职业与社会服务资源`：深圳应届生落户是否需要报到证的解答；深圳就业报到证办理所需材料说明
- 合并 `科技与投资观点`（1 条；`small:technology-and-investing`）→ `职业与社会服务资源`：硅谷王川的科技投资观点与个人博客主页
- 合并 `职业档案与人脉`（1 条；`small:professional-profiles`）→ `职业与社会服务资源`：李业章个人LinkedIn职业档案主页
- 合并 `自由职业与服务平台`（1 条；`small:freelance-work`）→ `职业与社会服务资源`：Fiverr卖家服务项目编辑与发布管理页面
- 合并 `自由职业平台`（2 条；`small:freelance-platforms`）→ `职业与社会服务资源`：Freelancer自由职业平台帮助支持分类中心；Upwork全球自由职业者与远程项目招聘平台
- 合并 `量化投资`（1 条；`small:quantitative-investing`）→ `职业与社会服务资源`：BigQuant量化投资研究方法与平台功能说明
- 合并 `金融投资工具`（1 条；`small:financial-tools`）→ `职业与社会服务资源`：FinceptTerminal：面向专业人士的金融情报终端
- 合并 `金融监管`（1 条；`small:financial-regulation`）→ `职业与社会服务资源`：国家金融监督管理总局官方网站与金融监管信息
- 合并 `金融计算工具`（1 条；`small:financial-calculators`）→ `职业与社会服务资源`：在线利息计算器：计算本金、利率与收益
- 合并 `高端人才外包招聘`（1 条；`small:talent-outsourcing`）→ `职业与社会服务资源`：Toptal 全球高端自由职业人才雇佣平台

### `04_工作与生活 / 职业、政务与金融 / 金融、投资、税务与信用`（23 个来源目录 / 29 条书签）
- 合并 `个人征信`（1 条；`small:personal-credit`）→ `金融、投资、税务与信用`：中国人民银行个人信用信息查询服务平台
- 合并 `个税与薪资计算`（1 条；`small:tax-and-payroll`）→ `金融、投资、税务与信用`：年终奖个税反算与所得税计算器（2020）
- 合并 `企业银行与财务`（1 条；`small:business-banking`）→ `金融、投资、税务与信用`：Mercury 面向初创企业与小企业的在线商业银行服务
- 合并 `保险业务平台`（1 条；`small:insurance-business`）→ `金融、投资、税务与信用`：中国人寿国寿e店业务平台扫码登录入口
- 合并 `保险行业信息`（1 条；`small:insurance-industry`）→ `金融、投资、税务与信用`：中国保险行业协会官方网站与行业资讯服务
- 合并 `创业融资与并购`（1 条；`small:startup-finance`）→ `金融、投资、税务与信用`：方创资本创业融资与企业并购财务顾问服务
- 合并 `医疗保险服务`（2 条；`small:health-insurance`）→ `金融、投资、税务与信用`：爱牙保障计划指定口腔保险门诊查询页面；2023年度上海沪惠保投保入口与常见问题解答
- 合并 `地方招商与园区`（1 条；`small:local-investment-promotion`）→ `金融、投资、税务与信用`：鄂州招商网产业扶持政策与工业园区招商信息
- 合并 `投资交易平台`（1 条；`small:investment-platforms`）→ `金融、投资、税务与信用`：RockFlow智能交易平台开户链接与投资服务主页
- 合并 `投资机构与资管公司`（1 条；`small:investment-firms`）→ `金融、投资、税务与信用`：GROW Investment Group投资集团官方网站
- 合并 `投资知识学习`（1 条；`small:investment-education`）→ `金融、投资、税务与信用`：Investopedia金融投资知识学习与术语解释平台
- 合并 `支付与金融服务`（2 条；`small:payments-and-finance`）→ `金融、投资、税务与信用`：BigQuant 人工智能量化投资研究与交易平台；PayPal 商家工具与收款账户偏好设置页面
- 合并 `支付与金融账户`（1 条；`small:payments-finance`）→ `金融、投资、税务与信用`：支付宝账户登录与个人支付服务入口
- 合并 `新加坡银行服务`（1 条；`small:singapore-banking`）→ `金融、投资、税务与信用`：华侨银行OCBC网上银行登录与账户管理入口
- 合并 `汽车金融计算`（2 条；`small:car-finance`）→ `金融、投资、税务与信用`：太平洋汽车购车贷款分期付款计算器页面；特斯拉中国汽车金融贷款月供计算服务页面
- 合并 `网上银行服务`（2 条；`small:online-banking`）→ `金融、投资、税务与信用`：中国银行个人网上银行登录服务页面；中国银行iGTB企业网上银行业务入口
- 合并 `美国企业与税务`（1 条；`small:us-business-tax`）→ `金融、投资、税务与信用`：美国企业在线申请EIN税号流程指南
- 合并 `美国信用卡`（1 条；`small:us-credit-cards`）→ `金融、投资、税务与信用`：美国信用卡新手申请入门与首卡选择推荐指南
- 合并 `财税记账服务`（1 条；`small:accounting-tax`）→ `金融、投资、税务与信用`：自记账零基础企业记账与报税服务
- 合并 `跨境银行与加密游牧`（2 条；`small:cross-border-banking`）→ `金融、投资、税务与信用`：加密游牧指南：在格鲁吉亚开设个人银行账户；香港银行账户开户与日常使用完整指南V1.5
- 合并 `量化金融与交易工具`（1 条；`small:quantitative-finance`）→ `金融、投资、税务与信用`：BigQuant QuantChat AI 量化知识库使用文档
- 合并 `银行金融服务`（1 条；`small:banking`）→ `金融、投资、税务与信用`：中国工商银行个人网上银行登录入口
- 合并 `香港银行开户`（2 条；`small:hong-kong-banking`）→ `金融、投资、税务与信用`：V2EX香港银行卡开户经验与流程总结讨论；香港多家银行开户与虚拟银行开卡实战经验分享

## 完整迁移对照表（1,246 个来源叶子目录，已执行）

| # | 当前路径 | 来源叶子目录 | 书签数 | 建议目标目录 | 逻辑键 | 书签标题 |
| ---: | --- | --- | ---: | --- | --- | --- |
| 1 | `01_技术与AI / 云服务、运维与网络安全` | Cloudflare Workers资源 | 1 | `Cloudflare 与边缘服务` | `small:cloudflare-workers` | Cloudflare Workers优秀开源项目持续收集 |
| 2 | `01_技术与AI / 云服务、运维与网络安全` | Cloudflare资源 | 1 | `Cloudflare 与边缘服务` | `small:cloudflare-resources` | Awesome Cloudflare：Cloudflare 服务资源与工具合集 |
| 3 | `01_技术与AI / 云服务、运维与网络安全` | 代理与网络运维 | 2 | `Cloudflare 与边缘服务` | `small:proxy-operations` | tcp-wss脚本卸载方法与社区讨论帖子；通过 Cloudflare Worker 与 Pages 部署免费 VLESS 节点教程 |
| 4 | `01_技术与AI / 云服务、运维与网络安全` | CI/CD 与 DevOps | 2 | `DevOps、CI/CD 与交付` | `small:cicd-and-devops` | 从零搭建持续集成与持续交付CI/CD流水线指南；KubeSphere图形化创建Jenkins流水线配置文档 |
| 5 | `01_技术与AI / 云服务、运维与网络安全` | DevOps 与持续集成 | 1 | `DevOps、CI/CD 与交付` | `small:devops-cicd` | 使用 Jenkins 自动构建与部署 Vue 项目教程 |
| 6 | `01_技术与AI / 云服务、运维与网络安全` | DevOps与容器技术 | 1 | `DevOps、CI/CD 与交付` | `small:devops-and-containers` | Docker Engine容器运行环境安装官方文档 |
| 7 | `01_技术与AI / 云服务、运维与网络安全` | DevOps与开源项目 | 2 | `DevOps、CI/CD 与交付` | `small:devops-open-source` | Jpom开源项目运维管理与发布平台；Dromara开源社区项目与技术生态官网 |
| 8 | `01_技术与AI / 云服务、运维与网络安全` | 开发运维与持续集成 | 1 | `DevOps、CI/CD 与交付` | `small:devops-tools` | 192-SIT后端应用持续集成Jenkins任务视图 |
| 9 | `01_技术与AI / 云服务、运维与网络安全` | 持续集成与持续交付 | 2 | `DevOps、CI/CD 与交付` | `small:ci-cd` | Zadig持续交付平台安装部署官方文档；Jenkins团队接入Zadig实现端到端软件交付教程 |
| 10 | `01_技术与AI / 云服务、运维与网络安全` | 部署与DevOps | 2 | `DevOps、CI/CD 与交付` | `small:deployment-devops` | Coolify：开源自托管应用部署与服务器管理平台；Vercel 前端云平台：构建与部署 Web 应用 |
| 11 | `01_技术与AI / 云服务、运维与网络安全` | Linux 命令与参考 | 1 | `Linux、NAS 与自建服务` | `small:linux-reference` | LinuxCool常用Linux命令手册与在线查询网站 |
| 12 | `01_技术与AI / 云服务、运维与网络安全` | Linux与终端 | 1 | `Linux、NAS 与自建服务` | `small:linux-and-terminal` | TTY终端设备概念、历史与Linux交互机制详解 |
| 13 | `01_技术与AI / 云服务、运维与网络安全` | Linux系统镜像与安装 | 1 | `Linux、NAS 与自建服务` | `small:linux-installation` | CentOS 8.4.2105 x86_64 安装 ISO 镜像目录 |
| 14 | `01_技术与AI / 云服务、运维与网络安全` | NAS与家庭服务器 | 2 | `Linux、NAS 与自建服务` | `small:nas-admin` | 局域网Synology DiskStation私有NAS管理后台；局域网Synology DiskStation群晖NAS管理登录界面 |
| 15 | `01_技术与AI / 云服务、运维与网络安全` | VPS 与云主机 | 1 | `云主机、域名与托管服务` | `small:vps-cloud-hosting` | DediPath 全托管 SSD VPS 云服务器产品页 |
| 16 | `01_技术与AI / 云服务、运维与网络安全` | VPS测评与选购 | 2 | `云主机、域名与托管服务` | `small:vps-resources` | TopVPS服务器主机测评与VPS选购资讯网站；TopVPS虚拟服务器推荐排行与测评汇总页面 |
| 17 | `01_技术与AI / 云服务、运维与网络安全` | VPS网络优化 | 2 | `云主机、域名与托管服务` | `small:vps-optimization` | Linux服务器启用BBR加速进行网络性能优化脚本；低性能VPS线路优化与网络视频传输加速方法 |
| 18 | `01_技术与AI / 云服务、运维与网络安全` | 主机服务与账户管理 | 1 | `云主机、域名与托管服务` | `small:hosting-accounts` | 2345.to服务客户中心与账户管理入口 |
| 19 | `01_技术与AI / 云服务、运维与网络安全` | 云平台与部署 | 2 | `云主机、域名与托管服务` | `small:cloud-platforms` | Sealos Cloud 云原生应用部署与云资源管理控制台；山海云企业级云计算服务器与VPS服务商 |
| 20 | `01_技术与AI / 云服务、运维与网络安全` | 云服务优惠讨论 | 1 | `云主机、域名与托管服务` | `small:cloud-services-discussions` | Hostloc论坛讨论谷歌云存储优惠活动 |
| 21 | `01_技术与AI / 云服务、运维与网络安全` | 云服务控制台 | 1 | `云主机、域名与托管服务` | `small:cloud-service-consoles` | 快手云StreamLake开发者服务控制台首页 |
| 22 | `01_技术与AI / 云服务、运维与网络安全` | 企业云服务 | 1 | `云主机、域名与托管服务` | `small:enterprise-cloud-services` | 龙雀云云助理开放门户账号登录入口 |
| 23 | `01_技术与AI / 云服务、运维与网络安全` | 域名与主机服务 | 1 | `云主机、域名与托管服务` | `small:domains-hosting` | NameSilo 低价域名注册与网站托管服务 |
| 24 | `01_技术与AI / 云服务、运维与网络安全` | 开发与自建服务 | 2 | `云主机、域名与托管服务` | `small:developer-self-hosting` | Bark iOS 设备自建推送通知服务使用入口；Bark 推送通知服务端的部署配置说明文档 |
| 25 | `01_技术与AI / 云服务、运维与网络安全` | 开发云服务 | 2 | `云主机、域名与托管服务` | `small:developer-cloud-services` | 网易云信即时通讯与音视频通信云服务平台；网易七鱼智能客服开发指南与接口文档概述 |
| 26 | `01_技术与AI / 云服务、运维与网络安全` | 服务器与硬件 | 1 | `云主机、域名与托管服务` | `small:servers-and-hardware` | 选购二手服务器的配置评估与避坑经验分享 |
| 27 | `01_技术与AI / 云服务、运维与网络安全` | 网站托管服务 | 1 | `云主机、域名与托管服务` | `small:web-hosting` | Serv00 免费托管与服务器服务官网 |
| 28 | `01_技术与AI / 云服务、运维与网络安全` | 网络服务购买 | 2 | `云主机、域名与托管服务` | `small:network-service-purchases` | Bandwagon Host 搬瓦工 VPS 套餐购物车配置页；Yiyo Networks 会员商店与套餐购买页面 |
| 29 | `01_技术与AI / 云服务、运维与网络安全` | 自托管与部署 | 2 | `云主机、域名与托管服务` | `small:self-hosting` | Zeabur部署WeWe RSS阅读器指南；懒猫微服Launcher应用启动器与服务管理页面 |
| 30 | `01_技术与AI / 云服务、运维与网络安全` | 下载与云存储自动化 | 1 | `云服务与运维综合资源` | `small:downloads-and-cloud-storage` | Aria2离线下载并通过Rclone自动上传OneDrive配置 |
| 31 | `01_技术与AI / 云服务、运维与网络安全` | 个人云与云桌面 | 1 | `云服务与运维综合资源` | `small:personal-cloud` | Puter 浏览器中的开源个人云桌面平台 |
| 32 | `01_技术与AI / 云服务、运维与网络安全` | 云原生调试与可观测性 | 1 | `云服务与运维综合资源` | `small:cloud-debugging` | Rookout 云原生应用实时动态调试平台 |
| 33 | `01_技术与AI / 云服务、运维与网络安全` | 云存储与效率工具 | 2 | `云服务与运维综合资源` | `small:cloud-productivity` | Dropbox个人文件管理与云端同步主页；Zoom视频会议账户个人资料设置页面 |
| 34 | `01_技术与AI / 云服务、运维与网络安全` | 云存储工具 | 1 | `云服务与运维综合资源` | `small:cloud-storage-tools` | NetDrive三跨平台网盘挂载本地磁盘软件 |
| 35 | `01_技术与AI / 云服务、运维与网络安全` | 云手机与多账号运营 | 2 | `云服务与运维综合资源` | `small:cloud-phone-operations` | 比特云手机跨境电商多账号运营平台；比特指纹浏览器云手机环境接口文档 |
| 36 | `01_技术与AI / 云服务、运维与网络安全` | 云端应用平台 | 1 | `云服务与运维综合资源` | `small:cloud-apps` | UZER.ME云端超级应用与在线软件运行平台 |
| 37 | `01_技术与AI / 云服务、运维与网络安全` | 云计算服务 | 1 | `云服务与运维综合资源` | `small:cloud-computing` | 顺网算力云云端计算资源服务平台 |
| 38 | `01_技术与AI / 云服务、运维与网络安全` | 工程仿真与云计算 | 1 | `云服务与运维综合资源` | `small:engineering-cloud` | 速石科技研发仿真云与高性能计算平台 |
| 39 | `01_技术与AI / 云服务、运维与网络安全` | 汽车安全测评 | 2 | `云服务与运维综合资源` | `small:auto-safety` | 中国保险汽车安全指数车辆碰撞测试结果详情；中国保险汽车安全指数最新动态与测评资讯 |
| 40 | `01_技术与AI / 云服务、运维与网络安全` | 系统启动与部署工具 | 2 | `云服务与运维综合资源` | `small:boot-and-deployment` | iVentoy局域网多系统镜像PXE启动部署工具；Ventoy Plugson网页端启动盘插件配置工具 |
| 41 | `01_技术与AI / 云服务、运维与网络安全` | 网盘与下载工具 | 1 | `云服务与运维综合资源` | `small:cloud-downloads` | 黑科云破解版自动注册账号与下载链接解析教程 |
| 42 | `01_技术与AI / 云服务、运维与网络安全` | 网盘资源与搜索 | 1 | `云服务与运维综合资源` | `small:cloud-drive-resources` | 找资源阿里云盘公开分享资源搜索引擎 |
| 43 | `01_技术与AI / 云服务、运维与网络安全` | 网盘资源搜索 | 2 | `云服务与运维综合资源` | `small:cloud-search` | 阿里云盘资源搜索与文件检索工具；千帆搜索聚合网盘文件与资源检索工具 |
| 44 | `01_技术与AI / 云服务、运维与网络安全` | 网盘资源搜索 | 1 | `云服务与运维综合资源` | `small:cloud-storage-resources` | 找资源阿里云盘公开资源搜索引擎 |
| 45 | `01_技术与AI / 云服务、运维与网络安全` | 网站部署与分析 | 2 | `云服务与运维综合资源` | `small:web-deployment-analytics` | Vercel 项目控制台与网站分析数据面板；PinMe：数秒发布静态网站的部署平台 |
| 46 | `01_技术与AI / 云服务、运维与网络安全` | 网络与 OpenWrt | 1 | `云服务与运维综合资源` | `small:networking-and-openwrt` | OpenWrt IPoE 宽带认证与动态鉴权配置实战教程 |
| 47 | `01_技术与AI / 云服务、运维与网络安全` | 网络与DNS | 2 | `云服务与运维综合资源` | `small:networking-and-dns` | Open vSwitch 虚拟交换机实践配置与原理教程；Sukka分享个人DNS配置方案与使用技巧文章 |
| 48 | `01_技术与AI / 云服务、运维与网络安全` | 网络与号码查询工具 | 2 | `云服务与运维综合资源` | `small:network-query-tools` | openGPS 高精度IP地址地理位置定位查询；IP138 手机号码归属地与电话号码查询 |
| 49 | `01_技术与AI / 云服务、运维与网络安全` | 网络代理与连接服务 | 1 | `云服务与运维综合资源` | `small:network-services` | 一元机场代理订阅与网络节点管理控制台 |
| 50 | `01_技术与AI / 云服务、运维与网络安全` | 网络脚本与规则资源 | 1 | `云服务与运维综合资源` | `small:network-scripts` | YBNET 模块插件脚本与去广告规则资源站 |
| 51 | `01_技术与AI / 云服务、运维与网络安全` | 网络访问工具 | 2 | `云服务与运维综合资源` | `small:network-access` | 突破防火长城网络访问工具与方法导航页；Cloudflare 1.1.1.1 加速网络与隐私 DNS 应用 |
| 52 | `01_技术与AI / 云服务、运维与网络安全` | 网络诊断工具 | 2 | `云服务与运维综合资源` | `small:network-diagnostics` | IPW IPv6 地址归属地与网络信息查询；GFW.Report网络连通性检测报告平台 |
| 53 | `01_技术与AI / 云服务、运维与网络安全` | 网络辅助软件 | 1 | `云服务与运维综合资源` | `small:network-software` | Watt Toolkit瓦特工具箱与Steam网络辅助软件官网 |
| 54 | `01_技术与AI / 云服务、运维与网络安全` | 软件行业与开发安全 | 1 | `云服务与运维综合资源` | `small:software-industry` | SonarSource 用户增长与开发安全市场机会分析 |
| 55 | `01_技术与AI / 云服务、运维与网络安全` | 云原生与微服务 | 2 | `监控、日志与可观测性` | `small:cloud-native-infrastructure` | Higress网关路由规则在线配置演示页面；Nacos 服务发现与配置管理快速入门指南 |
| 56 | `01_技术与AI / 云服务、运维与网络安全` | 云服务与基础设施 | 1 | `监控、日志与可观测性` | `small:cloud-and-infrastructure` | 火山引擎云服务管理控制台首页 |
| 57 | `01_技术与AI / 云服务、运维与网络安全` | 基础设施监控 | 1 | `监控、日志与可观测性` | `small:infrastructure-monitoring` | Netdata 高分辨率实时基础设施监控平台 |
| 58 | `01_技术与AI / 云服务、运维与网络安全` | 天气与灾害监测 | 1 | `监控、日志与可观测性` | `small:weather-monitoring` | 浙江台风路径实时发布与监测系统 |
| 59 | `01_技术与AI / 云服务、运维与网络安全` | 日志与可观测性 | 1 | `监控、日志与可观测性` | `small:logging-observability` | lnav 高级日志文件查看与分析工具 |
| 60 | `01_技术与AI / 云服务、运维与网络安全` | 网站监控 | 1 | `监控、日志与可观测性` | `small:web-monitoring` | changedetection.io 网站页面变化监控与通知告警工具 |
| 61 | `01_技术与AI / 云服务、运维与网络安全` | VPN 使用指南 | 1 | `网络代理与连通工具` | `small:vpn-guides` | 免费VPN选择推荐与使用指南文章 |
| 62 | `01_技术与AI / 云服务、运维与网络安全` | VPN 用户中心 | 2 | `网络代理与连通工具` | `small:vpn-account-centers` | 我们所向往VPN网络服务用户页面；iKuuu VPN 网络代理服务用户账户首页 |
| 63 | `01_技术与AI / 云服务、运维与网络安全` | 代理与网络工具 | 1 | `网络代理与连通工具` | `small:proxy-network-tools` | iDaili 免费网页在线代理服务入口 |
| 64 | `01_技术与AI / 云服务、运维与网络安全` | 代理工具官方文档 | 1 | `网络代理与连通工具` | `small:proxy-official-docs` | V2Ray 模块化代理程序下载安装官方指南 |
| 65 | `01_技术与AI / 云服务、运维与网络安全` | 代理工具项目 | 1 | `网络代理与连通工具` | `small:proxy-projects` | Shadowsocks 安全 SOCKS5 代理官方项目首页 |
| 66 | `01_技术与AI / 云服务、运维与网络安全` | 代理服务导航 | 2 | `网络代理与连通工具` | `small:proxy-navigation` | IBCN 网络代理服务导航与站点入口页面；品云机场导航中的超级跑车服务介绍页 |
| 67 | `01_技术与AI / 云服务、运维与网络安全` | 代理规则与网络资源 | 1 | `网络代理与连通工具` | `small:proxy-resources` | YFamily网络重写模块插件与去广告规则合集 |
| 68 | `01_技术与AI / 云服务、运维与网络安全` | 代理软件与项目 | 1 | `网络代理与连通工具` | `small:proxy-software` | V2Fly 官方项目网站与 V2Ray 开发信息 |
| 69 | `01_技术与AI / 云服务、运维与网络安全` | 开发者网络工具 | 2 | `网络代理与连通工具` | `small:developer-network-tools` | GitHub Proxy 镜像加速服务最新地址发布页；CLI Proxy API 的管理中心与接口代理配置 |
| 70 | `01_技术与AI / 云服务、运维与网络安全` | 网络与组网工具 | 2 | `网络代理与连通工具` | `small:networking` | VNT基于Rust构建的虚拟网络组网工具官网；WireGuard快速现代安全VPN隧道中文介绍网站 |
| 71 | `01_技术与AI / 云服务、运维与网络安全` | 网络访问工具指南 | 2 | `网络代理与连通工具` | `small:proxy-service-guides` | 电脑网络访问工具与VPN加速器选择综合指南；适用于中国地区的免费VPN服务优缺点比较指南 |
| 72 | `01_技术与AI / 云服务、运维与网络安全` | 隐私与VPN服务 | 2 | `网络代理与连通工具` | `small:privacy-vpn` | AdGuard VPN隐私保护服务中文欢迎与使用入口；AdGuard账户授权认证与我的账户登录页面 |
| 73 | `01_技术与AI / 云服务、运维与网络安全` | 隐私与网络工具 | 2 | `网络代理与连通工具` | `small:privacy-and-network-tools` | Virtual Browser在线虚拟浏览器服务；快代理企业级HTTP代理IP云服务与网络代理平台 |
| 74 | `01_技术与AI / 云服务、运维与网络安全` | Java认证与权限框架 | 1 | `网络安全、隐私与账号保护` | `small:java-security-frameworks` | Sa-Token Java 权限认证与会话管理框架 |
| 75 | `01_技术与AI / 云服务、运维与网络安全` | 业务安全与风控 | 2 | `网络安全、隐私与账号保护` | `small:security-risk-control` | 数美科技在线业务智能风控解决方案官网；数美智能视频文件识别接口开发文档与示例 |
| 76 | `01_技术与AI / 云服务、运维与网络安全` | 安全与风控服务 | 1 | `网络安全、隐私与账号保护` | `small:security-and-risk-management` | 数美科技在线业务风控与反欺诈解决方案官网 |
| 77 | `01_技术与AI / 云服务、运维与网络安全` | 安全漏洞与利用 | 1 | `网络安全、隐私与账号保护` | `small:security-vulnerabilities` | Exploit Database公开漏洞利用代码与安全漏洞数据库 |
| 78 | `01_技术与AI / 云服务、运维与网络安全` | 安全通信应用 | 1 | `网络安全、隐私与账号保护` | `small:secure-messaging` | SafeW安全即时通信应用官方网站 |
| 79 | `01_技术与AI / 云服务、运维与网络安全` | 安全防护工具 | 1 | `网络安全、隐私与账号保护` | `small:security-tools` | 火绒安全电脑防护与病毒查杀软件官网 |
| 80 | `01_技术与AI / 云服务、运维与网络安全` | 密码与安全管理 | 1 | `网络安全、隐私与账号保护` | `small:password-and-security` | 1Password 君王账户密码库全部项目管理页面 |
| 81 | `01_技术与AI / 云服务、运维与网络安全` | 应用安全 | 1 | `网络安全、隐私与账号保护` | `small:application-security` | 云鲨 RASP 应用运行时安全威胁防护管理平台 |
| 82 | `01_技术与AI / 云服务、运维与网络安全` | 移动通信安全 | 1 | `网络安全、隐私与账号保护` | `small:mobile-security` | 拦截猫iPhone骚扰电话与垃圾短信防护工具 |
| 83 | `01_技术与AI / 云服务、运维与网络安全` | 系统安全工具 | 1 | `网络安全、隐私与账号保护` | `small:system-security` | OpenArk 用于系统安全分析的开源反 Rootkit 工具 |
| 84 | `01_技术与AI / 云服务、运维与网络安全` | 网站安全检测 | 1 | `网络安全、隐私与账号保护` | `small:web-security` | Qualys SSL Labs：在线检测网站 SSL 与 TLS 安全配置 |
| 85 | `01_技术与AI / 云服务、运维与网络安全` | 网站检测与安全工具 | 1 | `网络安全、隐私与账号保护` | `small:website-security-tools` | Web Check网站安全隐私与技术信息检测工具 |
| 86 | `01_技术与AI / 云服务、运维与网络安全` | 网络安全产品 | 1 | `网络安全、隐私与账号保护` | `small:cybersecurity-products` | 积至网络Cyber Narrator网络叙事分析产品 |
| 87 | `01_技术与AI / 云服务、运维与网络安全` | 网络安全资讯与会议 | 1 | `网络安全、隐私与账号保护` | `small:cybersecurity-news-and-events` | Hack In The Box全球网络安全会议、新闻与研究资讯 |
| 88 | `01_技术与AI / 云服务、运维与网络安全` | 网络隐私与检测 | 1 | `网络安全、隐私与账号保护` | `small:network-privacy` | IPCheck：本机公网IP、WebRTC与DNS泄露检测工具 |
| 89 | `01_技术与AI / 云服务、运维与网络安全` | 虚拟化安全资讯 | 1 | `网络安全、隐私与账号保护` | `small:virtualization-security` | VMware多产品安全漏洞更新公告 |
| 90 | `01_技术与AI / 云服务、运维与网络安全` | 身份认证与账户安全 | 1 | `网络安全、隐私与账号保护` | `small:identity-security` | Duo Security已验证设备管理门户 |
| 91 | `01_技术与AI / 人工智能、模型与自动化` | AI写作与文本优化 | 2 | `AI 写作、办公与文档` | `small:ai-writing` | AiTxt小红书文案与职场文本智能生成助手；ReduceAI 去除文本AI痕迹与润色工具 |
| 92 | `01_技术与AI / 人工智能、模型与自动化` | AI办公与文档生成 | 1 | `AI 写作、办公与文档` | `small:ai-office` | 讯飞智文 AI在线生成PPT与Word文档 |
| 93 | `01_技术与AI / 人工智能、模型与自动化` | 人工智能学习与评测 | 2 | `AI 学习、研究与评测` | `small:ai-learning` | C-Eval中文大语言模型多学科能力评测排行榜；伯克利 CS188 2023 秋季人工智能导论课程 |
| 94 | `01_技术与AI / 人工智能、模型与自动化` | AI 工作流工具 | 1 | `AI 对话、助手与搜索` | `small:ai-workflows` | GPT AI Flow 个性化智能助手与工作自动化平台 |
| 95 | `01_技术与AI / 人工智能、模型与自动化` | AI 建站工具 | 2 | `AI 对话、助手与搜索` | `small:ai-website-builders` | Wegic 人工智能网站设计与生成应用；Wegic AI 网站设计与开发助手平台 |
| 96 | `01_技术与AI / 人工智能、模型与自动化` | AI 提示词资源 | 1 | `AI 对话、助手与搜索` | `small:ai-prompts` | PromptHero生成式AI绘图与对话提示词搜索 |
| 97 | `01_技术与AI / 人工智能、模型与自动化` | AI 搜索与问答 | 1 | `AI 对话、助手与搜索` | `small:ai-search` | 秘塔AI搜索：面向问答与信息检索的智能搜索引擎 |
| 98 | `01_技术与AI / 人工智能、模型与自动化` | AIGC产业分析 | 2 | `AI 对话、助手与搜索` | `small:aigc-industry-analysis` | 中国AIGC产业链概念龙头股与市场规模分析文章；AIGC产业爆发背景下科技公司人工智能专利排行榜 |
| 99 | `01_技术与AI / 人工智能、模型与自动化` | AI内容生成 | 1 | `AI 对话、助手与搜索` | `small:ai-content-generation` | Pollo AI 多模型人工智能创作平台首页 |
| 100 | `01_技术与AI / 人工智能、模型与自动化` | AI创作与社区 | 2 | `AI 对话、助手与搜索` | `small:ai-creation` | YouWare AI创作者Vibe Coding个人主页；YouWare在线AI创作项目编辑器页面 |
| 101 | `01_技术与AI / 人工智能、模型与自动化` | AI工作空间 | 2 | `AI 对话、助手与搜索` | `small:ai-workspaces` | Skywork AI Workspace Agents 智能工作空间平台；flowith 2.0 AI 知识驱动创作工作区 |
| 102 | `01_技术与AI / 人工智能、模型与自动化` | AI应用创作平台 | 1 | `AI 对话、助手与搜索` | `small:ai-app-platforms` | MyShell AI：创建分享并拥有AI生成应用的平台 |
| 103 | `01_技术与AI / 人工智能、模型与自动化` | AI浏览器 | 2 | `AI 对话、助手与搜索` | `small:ai-browsers` | Tabbit AI智能浏览器产品官网；Dia Browser：可与浏览器标签页对话的AI浏览器 |
| 104 | `01_技术与AI / 人工智能、模型与自动化` | AI浏览器与自动化 | 1 | `AI 对话、助手与搜索` | `small:ai-browser-tools` | Fellou 深度搜索与自动化智能浏览器 |
| 105 | `01_技术与AI / 人工智能、模型与自动化` | AI硬件与边缘计算 | 1 | `AI 对话、助手与搜索` | `small:ai-hardware` | NVIDIA Jetson嵌入式AI开发套件购买页面 |
| 106 | `01_技术与AI / 人工智能、模型与自动化` | AI硬件评估工具 | 1 | `AI 对话、助手与搜索` | `small:ai-hardware-tools` | CanIRun.ai本机硬件运行AI模型兼容性检测 |
| 107 | `01_技术与AI / 人工智能、模型与自动化` | AI营销工具 | 2 | `AI 对话、助手与搜索` | `small:ai-marketing-tools` | 社媒助手用户中心账号与服务管理页面；Outbrand自动化社交媒体内容生成平台 |
| 108 | `01_技术与AI / 人工智能、模型与自动化` | AI行业与技术文章 | 1 | `AI 对话、助手与搜索` | `small:ai-industry-articles` | Anthropic Engineering 官方工程技术文章集合 |
| 109 | `01_技术与AI / 人工智能、模型与自动化` | Claude Code文档 | 1 | `AI 对话、助手与搜索` | `small:claude-code-docs` | Claude Code中文快速开始与Homebrew安装文档 |
| 110 | `01_技术与AI / 人工智能、模型与自动化` | Claude使用资源 | 1 | `AI 对话、助手与搜索` | `small:claude-resources` | Claude指令集中文版本与提示词使用参考 |
| 111 | `01_技术与AI / 人工智能、模型与自动化` | MCP与AI开发工具 | 2 | `AI 对话、助手与搜索` | `small:mcp-and-ai-dev` | Glama开源MCP服务器目录与AI工具发现平台；MCP.so模型上下文协议服务器发现与目录平台 |
| 112 | `01_技术与AI / 人工智能、模型与自动化` | 人工智能与互联网博客 | 1 | `AI 对话、助手与搜索` | `small:ai-tech-blogs` | Jack Cui关注人工智能与互联网技术的个人博客 |
| 113 | `01_技术与AI / 人工智能、模型与自动化` | 人工智能产品与公司 | 2 | `AI 对话、助手与搜索` | `small:ai-companies` | 中科深智人工智能与数字内容技术官网；整数智能人工智能数据与技术服务平台官网 |
| 114 | `01_技术与AI / 人工智能、模型与自动化` | 烟台政务经济 | 1 | `AI 对话、助手与搜索` | `small:yantai-government` | 烟台市发展和改革委员会官方网站与政务信息 |
| 115 | `01_技术与AI / 人工智能、模型与自动化` | 生成式人工智能工具 | 1 | `AI 对话、助手与搜索` | `small:generative-ai` | Tiamat AI人工智能创意生成平台 |
| 116 | `01_技术与AI / 人工智能、模型与自动化` | 语音 AI 工具 | 1 | `AI 对话、助手与搜索` | `small:voice-ai-tools` | Voicebox：开源桌面端语音克隆应用 |
| 117 | `01_技术与AI / 人工智能、模型与自动化` | AI 工具导航 | 1 | `AI 工具导航与平台入口` | `small:ai-tool-directory` | Toolify中文AI工具导航与人工智能产品目录 |
| 118 | `01_技术与AI / 人工智能、模型与自动化` | AI 平台账户入口 | 1 | `AI 工具导航与平台入口` | `small:ai-platform-accounts` | Fenno 人工智能服务账户注册邀请页面 |
| 119 | `01_技术与AI / 人工智能、模型与自动化` | AI公司与产品官网 | 1 | `AI 工具导航与平台入口` | `small:ai-company-websites` | Moonshot AI 月之暗面人工智能产品官网 |
| 120 | `01_技术与AI / 人工智能、模型与自动化` | AI工具评测与导航 | 1 | `AI 工具导航与平台入口` | `small:ai-tools-reviews` | Help AIO无赞助AI中转站评测工具横评与教程导航 |
| 121 | `01_技术与AI / 人工智能、模型与自动化` | AI推理服务 | 1 | `AI 开发、模型与智能体` | `small:ai-inference-services` | Inceptron高性价比人工智能推理算力服务平台 |
| 122 | `01_技术与AI / 人工智能、模型与自动化` | AI智能体学习资料 | 2 | `AI 开发、模型与智能体` | `small:ai-agent-learning` | Datawhale 通用智能体入门开源教程目录；Datawhale 通用智能体教程第一章环境安装配置 |
| 123 | `01_技术与AI / 人工智能、模型与自动化` | AI智能体开发 | 1 | `AI 开发、模型与智能体` | `small:ai-agent-development` | Coze扣子智能体开发平台使用文档中心 |
| 124 | `01_技术与AI / 人工智能、模型与自动化` | AI智能体记忆 | 2 | `AI 开发、模型与智能体` | `small:ai-agent-memory` | OpenMem：为智能体提供长期记忆能力的平台；OpenMem 文档：OpenClaw 的 Hermes 本地记忆插件 |
| 125 | `01_技术与AI / 人工智能、模型与自动化` | AI编程学习资料 | 1 | `AI 开发、模型与智能体` | `small:ai-coding-learning` | Claude Code 交互式模拟器与入门学习练习 |
| 126 | `01_技术与AI / 人工智能、模型与自动化` | 本地AI智能体服务 | 2 | `AI 开发、模型与智能体` | `small:local-ai-agent-services` | 本地部署 Hermes Agent 会话管理页面；本地 Hermes 智能体记忆服务管理界面 |
| 127 | `01_技术与AI / 数据、硬件与系统工具` | SEO与网站分析 | 1 | `数据采集、分析与可视化` | `small:seo-and-analytics` | Prelaunch.online网站SEO优化建议分析报告 |
| 128 | `01_技术与AI / 数据、硬件与系统工具` | 互联网统计资料 | 1 | `数据采集、分析与可视化` | `small:internet-statistics` | 中国互联网络信息中心官方网络发展统计资料 |
| 129 | `01_技术与AI / 数据、硬件与系统工具` | 产品数据分析 | 2 | `数据采集、分析与可视化` | `small:product-analytics` | OpenPanel开源产品数据分析与Mixpanel替代方案；Polyscore产品用户分析与智能市场洞察系统 |
| 130 | `01_技术与AI / 数据、硬件与系统工具` | 地方经济统计 | 1 | `数据采集、分析与可视化` | `small:local-economic-statistics` | 2022年青岛市国民经济和社会发展统计公报 |
| 131 | `01_技术与AI / 数据、硬件与系统工具` | 媒体资料与数据工具 | 1 | `数据采集、分析与可视化` | `small:media-data-tools` | JavHelper 影视演员信息抓取软件介绍与下载页面 |
| 132 | `01_技术与AI / 数据、硬件与系统工具` | 数据分析与指标 | 1 | `数据采集、分析与可视化` | `small:analytics` | Lecoq嵌入式用户指标数据展示页面 |
| 133 | `01_技术与AI / 数据、硬件与系统工具` | 数据分析与统计 | 2 | `数据采集、分析与可视化` | `small:data-analysis` | 中国统计网数据分析行业资讯与统计技能学习资源；199IT 大数据工具、报告与数据资源导航 |
| 134 | `01_技术与AI / 数据、硬件与系统工具` | 数据服务机构 | 1 | `数据采集、分析与可视化` | `small:data-services` | 人民数据管理有限公司机构介绍与业务信息 |
| 135 | `01_技术与AI / 数据、硬件与系统工具` | 数据采集工具 | 1 | `数据采集、分析与可视化` | `small:data-collection-tools` | 小红书内容采集集成管理平台首页 |
| 136 | `01_技术与AI / 数据、硬件与系统工具` | 测试资料生成工具 | 1 | `数据采集、分析与可视化` | `small:test-data-generators` | 菲律宾地址、身份信息与信用卡信息生成器 |
| 137 | `01_技术与AI / 数据、硬件与系统工具` | 算法与数据结构 | 2 | `数据采集、分析与可视化` | `small:algorithms-and-data-structures` | 柳婼程序猿笔记：算法与软件开发学习博客；labuladong算法小抄：数据结构与算法解题指南 |
| 138 | `01_技术与AI / 数据、硬件与系统工具` | 统计分析软件 | 1 | `数据采集、分析与可视化` | `small:statistics-software` | 华军软件园IBM SPSS Statistics统计软件下载安装页 |
| 139 | `01_技术与AI / 数据、硬件与系统工具` | 网站流量与SEO分析 | 2 | `数据采集、分析与可视化` | `small:website-analytics` | Similarweb 网站流量与竞争分析平台；爱站网 prpr.xprpr.io 网站综合数据查询 |
| 140 | `01_技术与AI / 数据、硬件与系统工具` | 金融数据工具 | 2 | `数据采集、分析与可视化` | `small:financial-data-tools` | 万得Wind金融终端数据分析与投资研究服务；雪球财经投资研究工具与市场信息导航页面 |
| 141 | `01_技术与AI / 数据、硬件与系统工具` | BT下载资源 | 2 | `文件、下载与格式转换` | `small:bt-resources` | RuTracker 俄语资源种子论坛与下载索引；TrackersList 全量BitTorrent Tracker列表文件 |
| 142 | `01_技术与AI / 数据、硬件与系统工具` | 下载与云存储服务 | 1 | `文件、下载与格式转换` | `small:download-and-storage` | Premiumize 多网盘聚合下载与云端服务平台 |
| 143 | `01_技术与AI / 数据、硬件与系统工具` | 下载工具社区 | 1 | `文件、下载与格式转换` | `small:download-community` | 唧唧社区与女装主题论坛入口 |
| 144 | `01_技术与AI / 数据、硬件与系统工具` | 媒体下载工具 | 1 | `文件、下载与格式转换` | `small:media-downloaders` | 推特X视频图片GIF与音频下载工具 |
| 145 | `01_技术与AI / 数据、硬件与系统工具` | 客户端软件下载 | 1 | `文件、下载与格式转换` | `small:client-downloads` | macwk-client客户端软件下载与服务入口 |
| 146 | `01_技术与AI / 数据、硬件与系统工具` | 数据备份工具 | 1 | `文件、下载与格式转换` | `small:data-backup-tools` | MH-Studio微信朋友圈采集与备份工具下载 |
| 147 | `01_技术与AI / 数据、硬件与系统工具` | 文件传输与分享 | 1 | `文件、下载与格式转换` | `small:file-sharing` | FileCodeBox 文件快递柜临时文件上传分享与取件服务 |
| 148 | `01_技术与AI / 数据、硬件与系统工具` | 文件共享与传输 | 1 | `文件、下载与格式转换` | `small:file-sharing-tools` | ShareDrop浏览器端局域网点对点文件共享工具 |
| 149 | `01_技术与AI / 数据、硬件与系统工具` | 文件同步与备份 | 1 | `文件、下载与格式转换` | `small:file-sync-backup` | GoodSync 文件同步、备份与跨设备传输软件 |
| 150 | `01_技术与AI / 数据、硬件与系统工具` | 文件管理工具 | 1 | `文件、下载与格式转换` | `small:file-utilities` | dupeGuru跨平台重复文件扫描清理工具 |
| 151 | `01_技术与AI / 数据、硬件与系统工具` | 磁力工具资讯 | 2 | `文件、下载与格式转换` | `small:torrent-guides` | Saber酱博客整理的磁链相关文章标签页；不死鸟BT磁力搜索引擎索引导航 |
| 152 | `01_技术与AI / 数据、硬件与系统工具` | 资源下载与分享 | 2 | `文件、下载与格式转换` | `small:resource-downloads` | 大熊分享站软件课程影视资源聚合首页；大熊分享站综合下载资源分类页面 |
| 153 | `01_技术与AI / 数据、硬件与系统工具` | NAS与私有云远程访问 | 2 | `浏览器、自动化与效率工具` | `small:nas-remote-access` | 飞牛fnOS私有云FN Connect远程访问入口；飞牛私有云fnOS的FN Connect远程访问服务 |
| 154 | `01_技术与AI / 数据、硬件与系统工具` | OCR与文档智能 | 1 | `浏览器、自动化与效率工具` | `small:ocr-and-document-ai` | Tesseract OCR五代接口与开发者API参考文档 |
| 155 | `01_技术与AI / 数据、硬件与系统工具` | OCR文字识别 | 1 | `浏览器、自动化与效率工具` | `small:ocr-tools` | 在线OCR文字识别与图片文字转换工具 |
| 156 | `01_技术与AI / 数据、硬件与系统工具` | RPA 与流程自动化 | 2 | `浏览器、自动化与效率工具` | `small:rpa-automation` | 来也 RPA 机器人流程自动化产品文档；UiBot 开发者指南中的 RPA 预备知识 |
| 157 | `01_技术与AI / 数据、硬件与系统工具` | 个人自动化 | 1 | `浏览器、自动化与效率工具` | `small:personal-automation` | 通过 GitHub 快捷指令自动记录每日起床时间的方法 |
| 158 | `01_技术与AI / 数据、硬件与系统工具` | 工业自动化 | 1 | `浏览器、自动化与效率工具` | `small:industrial-automation` | 基恩士中国工业自动化产品与解决方案官网 |
| 159 | `01_技术与AI / 数据、硬件与系统工具` | 浏览器与网页技术 | 2 | `浏览器、自动化与效率工具` | `small:browsers` | Ladybird：独立开发的开源现代网页浏览器项目；LemurBrowser浏览器新版本测试页面 |
| 160 | `01_技术与AI / 数据、硬件与系统工具` | 浏览器使用指南 | 1 | `浏览器、自动化与效率工具` | `small:browser-guides` | 从 Chrome 浏览器迁移书签和数据到 Safari 教程 |
| 161 | `01_技术与AI / 数据、硬件与系统工具` | 浏览器测试工具 | 1 | `浏览器、自动化与效率工具` | `small:browser-testing` | Chrome for Testing 浏览器测试版本下载与可用性列表 |
| 162 | `01_技术与AI / 数据、硬件与系统工具` | 网页自动化与采集 | 1 | `浏览器、自动化与效率工具` | `small:web-automation` | BrowserAct 无代码 AI 网页爬虫与自动化采集工具 |
| 163 | `01_技术与AI / 数据、硬件与系统工具` | 远程工作与招聘 | 2 | `浏览器、自动化与效率工具` | `small:remote-work` | GitWork：IT研发团队远程工作与程序员兼职平台；Upwork为自由职业者推荐的最佳远程工作机会 |
| 164 | `01_技术与AI / 数据、硬件与系统工具` | NAS与存储工具 | 1 | `硬件、设备与性能评测` | `small:nas-and-storage` | 群晖 DS918+ DSM 7.0 系统与实用工具下载中心 |
| 165 | `01_技术与AI / 数据、硬件与系统工具` | 外设性能测试 | 1 | `硬件、设备与性能评测` | `small:peripheral-testing` | CPS Check 在线鼠标回报率与轮询率检测工具 |
| 166 | `01_技术与AI / 数据、硬件与系统工具` | 外设配置工具 | 1 | `硬件、设备与性能评测` | `small:peripheral-tools` | 罗技Logi Options+设备配置软件官方下载页面 |
| 167 | `01_技术与AI / 数据、硬件与系统工具` | 显卡购买与行情 | 2 | `硬件、设备与性能评测` | `small:gpu-shopping` | NVIDIA 美国官网 RTX 3070 显卡购买页面；Best Buy 英伟达 RTX 3090 Ti 显卡商品页面 |
| 168 | `01_技术与AI / 数据、硬件与系统工具` | 显示器评测与选购 | 1 | `硬件、设备与性能评测` | `small:display-reviews` | TFT Central 显示器评测参数资讯与选购指南 |
| 169 | `01_技术与AI / 数据、硬件与系统工具` | 显示面板参数查询 | 1 | `硬件、设备与性能评测` | `small:display-hardware` | 屏库 Panelook 全球液晶显示屏规格参数查询平台 |
| 170 | `01_技术与AI / 数据、硬件与系统工具` | 电脑硬件资讯 | 2 | `硬件、设备与性能评测` | `small:computer-hardware` | 三星980 Pro固态硬盘0E故障与数据备份提醒；TechPowerUp硬件资讯显卡数据库与性能评测平台 |
| 171 | `01_技术与AI / 数据、硬件与系统工具` | 硬件与显示测试 | 1 | `硬件、设备与性能评测` | `small:hardware-testing` | TestUFO 显示器刷新率与运动模糊在线测试 |
| 172 | `01_技术与AI / 数据、硬件与系统工具` | 硬件厂商与产品 | 1 | `硬件、设备与性能评测` | `small:hardware-companies` | 摩尔线程国产GPU与图形计算产品官方网站 |
| 173 | `01_技术与AI / 数据、硬件与系统工具` | 硬件参数与性能对比 | 2 | `硬件、设备与性能评测` | `small:hardware-comparisons` | AMD锐龙7 7840HS与锐龙9 7940HS性能规格对比；TopCPU处理器显卡规格性能评级与型号对比 |
| 174 | `01_技术与AI / 数据、硬件与系统工具` | 硬件参数与性能对比 | 1 | `硬件、设备与性能评测` | `small:hardware-reference` | CPUBoss桌面与移动处理器性能参数对比工具 |
| 175 | `01_技术与AI / 数据、硬件与系统工具` | 硬件故障排查 | 1 | `硬件、设备与性能评测` | `small:hardware-troubleshooting` | 联想社区解决Radeon驱动与设置版本不匹配问题 |
| 176 | `01_技术与AI / 数据、硬件与系统工具` | 私有云与NAS管理 | 2 | `硬件、设备与性能评测` | `small:nas-management` | homenas 飞牛私有云 fnOS 家庭服务器首页；Immich 私有化照片与相册管理页面 |
| 177 | `01_技术与AI / 数据、硬件与系统工具` | Layui组件与扩展 | 1 | `设备、系统与平台服务` | `small:layui-ecosystem` | Layui第三方扩展组件与插件平台 |
| 178 | `01_技术与AI / 数据、硬件与系统工具` | Mac 效率工具 | 1 | `设备、系统与平台服务` | `small:mac-utilities` | TabTab 适用于 Mac 的窗口与标签增强管理工具 |
| 179 | `01_技术与AI / 数据、硬件与系统工具` | Mac媒体工具 | 1 | `设备、系统与平台服务` | `small:macos-media` | IINA：面向 macOS 的现代开源媒体播放器 |
| 180 | `01_技术与AI / 数据、硬件与系统工具` | Mac应用 | 1 | `设备、系统与平台服务` | `small:mac-apps` | Wallspace适用于macOS的动态实时壁纸应用 |
| 181 | `01_技术与AI / 数据、硬件与系统工具` | Mac文件工具 | 2 | `设备、系统与平台服务` | `small:macos-file-tools` | MacZip：专为 macOS 设计的文件压缩与解压软件；QSpace：支持多面板操作的 macOS Finder 替代品 |
| 182 | `01_技术与AI / 数据、硬件与系统工具` | Mac虚拟机工具 | 2 | `设备、系统与平台服务` | `small:macos-virtualization` | Parallels Desktop 19 启动器破解版下载与使用说明；Parallels Desktop 19 永久授权破解激活教程 |
| 183 | `01_技术与AI / 数据、硬件与系统工具` | Windows优化工具 | 1 | `设备、系统与平台服务` | `small:windows-tools` | BoosterX：面向游戏玩家的 Windows 系统优化工具介绍 |
| 184 | `01_技术与AI / 数据、硬件与系统工具` | Windows故障排除 | 2 | `设备、系统与平台服务` | `small:windows-troubleshooting` | Windows账户登录错误0x80190001解决教程；Windows 10中Microsoft Edge无法打开解决方法 |
| 185 | `01_技术与AI / 数据、硬件与系统工具` | Windows美化工具 | 1 | `设备、系统与平台服务` | `small:windows-customization` | TranslucentTB任务栏透明化工具下载页面 |
| 186 | `01_技术与AI / 数据、硬件与系统工具` | Windows软件管理 | 1 | `设备、系统与平台服务` | `small:windows-software-management` | Chocolatey Windows 命令行软件包管理器官方网站 |
| 187 | `01_技术与AI / 数据、硬件与系统工具` | iOS应用发现 | 1 | `设备、系统与平台服务` | `small:ios-app-discovery` | Departures平台探索与分享TestFlight测试应用 |
| 188 | `01_技术与AI / 数据、硬件与系统工具` | iOS应用管理 | 1 | `设备、系统与平台服务` | `small:ios-app-management` | 爱思助手IPA应用签名操作图文教程 |
| 189 | `01_技术与AI / 数据、硬件与系统工具` | iOS越狱资源 | 1 | `设备、系统与平台服务` | `small:ios-jailbreak` | Jailbreaks.app 苹果设备越狱工具与资源导航 |
| 190 | `01_技术与AI / 数据、硬件与系统工具` | macOS 与黑苹果 | 2 | `设备、系统与平台服务` | `small:macos-hackintosh` | 黑果小兵笔记本 Hackintosh 长期维护机型清单；macOS 应用提示已损坏无法打开的修复方法 |
| 191 | `01_技术与AI / 数据、硬件与系统工具` | macOS 效率工具 | 1 | `设备、系统与平台服务` | `small:macos-utilities` | DockX桌面程序坞与菜单栏管理工具 |
| 192 | `01_技术与AI / 数据、硬件与系统工具` | 一加资源 | 2 | `设备、系统与平台服务` | `small:oneplus-resources` | 一加 OnePlus 品牌相关网址导航与资源入口；一加社区关于 OnePlus 12 使用谷歌应用的讨论帖 |
| 193 | `01_技术与AI / 数据、硬件与系统工具` | 内部企业管理系统 | 2 | `设备、系统与平台服务` | `small:internal-business-systems` | 至恒融兴EMP企业管理平台首页入口；至恒融兴企业管理平台登录会话页面 |
| 194 | `01_技术与AI / 数据、硬件与系统工具` | 创意作品集 | 2 | `设备、系统与平台服务` | `small:creative-portfolios` | Cervantes Wu 自由艺术家个人作品集；dearpluto 创意项目个人网站首页 |
| 195 | `01_技术与AI / 数据、硬件与系统工具` | 创意工作室作品 | 2 | `设备、系统与平台服务` | `small:creative-studios` | THE LINE伦敦动画工作室官方作品展示；ILA创意工作室品牌与视觉作品集网站 |
| 196 | `01_技术与AI / 数据、硬件与系统工具` | 多边外交资料 | 1 | `设备、系统与平台服务` | `small:multilateral-diplomacy` | 外交部中国与国际组织及会议关系资料页面 |
| 197 | `01_技术与AI / 数据、硬件与系统工具` | 安卓客户端资源 | 1 | `设备、系统与平台服务` | `small:android-client-downloads` | 机场网盘安卓客户端资源分享目录 |
| 198 | `01_技术与AI / 数据、硬件与系统工具` | 安卓玩机社区 | 2 | `设备、系统与平台服务` | `small:android-communities` | MIUI 官方论坛与小米手机系统交流社区；XDA Developers 华为 Mate 30 Pro 玩机开发论坛 |
| 199 | `01_技术与AI / 数据、硬件与系统工具` | 宏观经济与市场数据 | 1 | `设备、系统与平台服务` | `small:macro-and-market-data` | Trading Economics全球宏观经济指标与市场数据平台 |
| 200 | `01_技术与AI / 数据、硬件与系统工具` | 宏观经济与投资数据 | 1 | `设备、系统与平台服务` | `small:macro-data` | 中投大数据宏观经济与投资数据查询平台 |
| 201 | `01_技术与AI / 数据、硬件与系统工具` | 并发与系统原理 | 1 | `设备、系统与平台服务` | `small:concurrency-and-systems` | Russ Cox关于并发程序内存模型与内存访问规则的文章 |
| 202 | `01_技术与AI / 数据、硬件与系统工具` | 操作系统下载 | 1 | `设备、系统与平台服务` | `small:operating-systems` | Ubuntu服务器版ARM架构系统官方下载页面 |
| 203 | `01_技术与AI / 数据、硬件与系统工具` | 硬件与驱动下载 | 1 | `设备、系统与平台服务` | `small:hardware-drivers` | NVIDIA GeForce显卡驱动程序官方下载入口 |
| 204 | `01_技术与AI / 数据、硬件与系统工具` | 系统安装与启动工具 | 1 | `设备、系统与平台服务` | `small:system-installation` | Ventoy开源多系统启动U盘制作工具中文官网 |
| 205 | `01_技术与AI / 数据、硬件与系统工具` | 系统维护与数据恢复 | 2 | `设备、系统与平台服务` | `small:system-maintenance` | 易我科技数据恢复、备份与磁盘分区管理工具官网；EaseUS Data Recovery Wizard数据恢复软件资源页面 |
| 206 | `01_技术与AI / 数据、硬件与系统工具` | 系统维护与磁盘工具 | 2 | `设备、系统与平台服务` | `small:system-utilities` | DiskGenius磁盘分区与数据恢复软件官方下载；易我磁盘拷贝硬盘克隆与系统迁移软件 |
| 207 | `01_技术与AI / 数据、硬件与系统工具` | 苹果生态资讯与资源 | 1 | `设备、系统与平台服务` | `small:apple-ecosystem` | 玩转苹果苹果产品软件资讯与资源平台 |
| 208 | `01_技术与AI / 数据、硬件与系统工具` | 苹果设备与macOS | 2 | `设备、系统与平台服务` | `small:macos-apple` | 订购M1 Pro十六英寸MacBook Pro后的使用思考；MediaFire 下载 macOS Sonoma 系统 ISO 镜像 |
| 209 | `01_技术与AI / 数据、硬件与系统工具` | 驱动与硬件支持 | 2 | `设备、系统与平台服务` | `small:drivers-and-hardware` | AMD Radeon RX五八零显卡驱动与支持页面；雷柏V21S鼠标驱动程序下载中心 |
| 210 | `01_技术与AI / 数据、硬件与系统工具` | Telegram 社群与数据 | 1 | `通信、网络与数字身份` | `small:telegram-communities` | TGStat影视分享Telegram群组数据统计页面 |
| 211 | `01_技术与AI / 数据、硬件与系统工具` | eSIM通信服务 | 1 | `通信、网络与数字身份` | `small:esim-services` | EasyEUICC应用下载与eUICC购买服务平台 |
| 212 | `01_技术与AI / 数据、硬件与系统工具` | MBTI 人格分析 | 2 | `通用软件与系统资源` | `small:mbti-personality` | 16Personalities总经理ESTJ人格特点与分析；16Personalities调停者INFP人格特点与分析 |
| 213 | `01_技术与AI / 数据、硬件与系统工具` | RAG与知识库系统 | 2 | `通用软件与系统资源` | `small:rag-and-knowledge-base` | FIT2CLOUD开源社区MaxKB产品入门指南；MaxKB开源大模型RAG知识库问答系统官网 |
| 214 | `01_技术与AI / 数据、硬件与系统工具` | 主机系统固件 | 1 | `通用软件与系统资源` | `small:console-firmware` | PlayStation官方PS3系统软件固件更新支持页 |
| 215 | `01_技术与AI / 数据、硬件与系统工具` | 人力资源管理系统 | 2 | `通用软件与系统资源` | `small:hr-management` | 北森iTalent一体化人才管理云平台登录入口；聘聘云一体化智能人力资源管理系统官网 |
| 216 | `01_技术与AI / 数据、硬件与系统工具` | 健康与公共数据 | 1 | `通用软件与系统资源` | `small:health-data` | 约翰霍普金斯大学COVID-19全球疫情地图 |
| 217 | `01_技术与AI / 数据、硬件与系统工具` | 公共数据查询 | 2 | `通用软件与系统资源` | `small:public-data` | 国家药品监督相关数据库查询页面；中国药品电子监管网官方网站首页 |
| 218 | `01_技术与AI / 数据、硬件与系统工具` | 加密货币市场数据 | 1 | `通用软件与系统资源` | `small:cryptocurrency-data` | 币安ADA季度合约每日盘口深度历史数据目录 |
| 219 | `01_技术与AI / 数据、硬件与系统工具` | 商业与创业数据 | 1 | `通用软件与系统资源` | `small:business-data` | IT桔子新经济行业倒闭公司数据库查询 |
| 220 | `01_技术与AI / 数据、硬件与系统工具` | 城市生活成本数据 | 1 | `通用软件与系统资源` | `small:city-cost-data` | Numbeo北京生活成本、物价与消费水平统计 |
| 221 | `01_技术与AI / 数据、硬件与系统工具` | 天气与气象数据 | 1 | `通用软件与系统资源` | `small:weather-data` | 上海二零二二年四月历史天气查询 |
| 222 | `01_技术与AI / 数据、硬件与系统工具` | 微信数据工具 | 2 | `通用软件与系统资源` | `small:wechat-tools` | WxDatViewer微信聊天图片查看导出工具；楼月微信聊天记录恢复工具下载页面 |
| 223 | `01_技术与AI / 数据、硬件与系统工具` | 手机购物 | 2 | `通用软件与系统资源` | `small:mobile-shopping` | 丰泽商城苹果iPhone 17 Pro Max商品页；csl香港网店苹果iPhone产品专区 |
| 224 | `01_技术与AI / 数据、硬件与系统工具` | 手机通信套餐 | 1 | `通用软件与系统资源` | `small:mobile-plans` | T-Mobile三美元预付费月租套餐价值分析 |
| 225 | `01_技术与AI / 数据、硬件与系统工具` | 政府开放数据 | 1 | `通用软件与系统资源` | `small:government-open-data` | 烟台市公共数据开放平台数据资源查询入口 |
| 226 | `01_技术与AI / 数据、硬件与系统工具` | 新媒体与内容数据 | 2 | `通用软件与系统资源` | `small:content-platform-data` | 新榜新媒体账号排行榜与内容传播数据分析；VidStatsX在线视频与YouTube频道数据统计平台 |
| 227 | `01_技术与AI / 数据、硬件与系统工具` | 电脑硬件与系统维护 | 1 | `通用软件与系统资源` | `small:pc-maintenance` | 图吧工具箱纯净电脑硬件检测维护工具集 |
| 228 | `01_技术与AI / 数据、硬件与系统工具` | 电脑装机指南 | 1 | `通用软件与系统资源` | `small:pc-building` | 英特尔游戏电脑装机配置与组装教程 |
| 229 | `01_技术与AI / 数据、硬件与系统工具` | 电脑软件工具 | 1 | `通用软件与系统资源` | `small:software-utilities` | Patch My PC Home Updater 家用电脑软件自动更新工具 |
| 230 | `01_技术与AI / 数据、硬件与系统工具` | 自托管存储与文件管理 | 1 | `通用软件与系统资源` | `small:self-hosted-storage` | AList 多存储挂载与文件列表程序中文使用文档 |
| 231 | `01_技术与AI / 数据、硬件与系统工具` | 自由职业与远程工作 | 1 | `通用软件与系统资源` | `small:freelancing` | Upwork自由职业项目沟通消息房间 |
| 232 | `01_技术与AI / 数据、硬件与系统工具` | 虚拟化与系统资源 | 2 | `通用软件与系统资源` | `small:virtualization-resources` | Windows 10 中 Hyper-V 虚拟机无法联网的排障教程；Sysin System Inside虚拟化与系统资源汇总 |
| 233 | `01_技术与AI / 数据、硬件与系统工具` | 设备固件与系统 | 1 | `通用软件与系统资源` | `small:device-firmware` | 一加手机系统固件下载与软件更新支持页面 |
| 234 | `01_技术与AI / 数据、硬件与系统工具` | 资源下载论坛 | 1 | `通用软件与系统资源` | `small:resource-forums` | RuTracker 俄语资源种子论坛首页 |
| 235 | `01_技术与AI / 数据、硬件与系统工具` | 隐私与匿名工具 | 1 | `通用软件与系统资源` | `small:privacy-tools` | Tor Project浏览器下载完成确认页面 |
| 236 | `01_技术与AI / 数据、硬件与系统工具` | 隐私与去中心化网络 | 2 | `通用软件与系统资源` | `small:privacy-and-decentralization` | PrivacysClub 隐私保护工具与网络隐私信息汇总站点；Freenet去中心化匿名网络与抗审查项目官网 |
| 237 | `01_技术与AI / 数据、硬件与系统工具` | 隐私与搜索工具 | 2 | `通用软件与系统资源` | `small:privacy-and-search` | 夸克浏览器官方网站与智能搜索服务入口；DuckDuckGo 隐私浏览器扩展与移动应用介绍页 |
| 238 | `01_技术与AI / 编程开发与开源` | CSS布局参考 | 1 | `前端与 Web 开发` | `small:css-layout-reference` | CSS Layout常用网页布局代码示例集合 |
| 239 | `01_技术与AI / 编程开发与开源` | CSS预处理器 | 1 | `前端与 Web 开发` | `small:css-preprocessors` | Sass中文网CSS预处理器语言学习文档 |
| 240 | `01_技术与AI / 编程开发与开源` | JavaScript 与 Node.js | 2 | `前端与 Web 开发` | `small:javascript-node` | npm官方JavaScript包管理与依赖检索平台；Node.js中文官方网站与JavaScript运行时资源 |
| 241 | `01_技术与AI / 编程开发与开源` | JavaScript与Node开发 | 1 | `前端与 Web 开发` | `small:javascript-development` | Node.js JavaScript 运行时官方网站与版本下载 |
| 242 | `01_技术与AI / 编程开发与开源` | JavaScript基础库 | 2 | `前端与 Web 开发` | `small:javascript-libraries` | jQuery官方库下载与版本发布页面；Day.js 中文网日期时间处理 JavaScript 库文档 |
| 243 | `01_技术与AI / 编程开发与开源` | JavaScript生态 | 2 | `前端与 Web 开发` | `small:javascript-ecosystem` | Yarn JavaScript 包管理器官方网站与使用资源；Deno新一代JavaScript与TypeScript运行时官网 |
| 244 | `01_技术与AI / 编程开发与开源` | React与前端开发 | 1 | `前端与 Web 开发` | `small:react-and-frontend` | React技术揭秘：从源码理解协调、渲染与提交流程 |
| 245 | `01_技术与AI / 编程开发与开源` | React开发 | 1 | `前端与 Web 开发` | `small:react` | React 官方中文文档与用户界面开发学习指南 |
| 246 | `01_技术与AI / 编程开发与开源` | Web 开发文档 | 2 | `前端与 Web 开发` | `small:web-documentation` | MDN CSS border 边框属性中文参考文档；MDN Web 开发技术中文文档导航 |
| 247 | `01_技术与AI / 编程开发与开源` | 前端与Web开发 | 2 | `前端与 Web 开发` | `small:web-development` | Hexo快速静态博客框架中文官方网站与文档；Vite 官方中文文档开始指南与前端构建入门 |
| 248 | `01_技术与AI / 编程开发与开源` | 前端工程化 | 1 | `前端与 Web 开发` | `small:frontend-engineering` | 当前生产环境部署ES2015及更高版本JavaScript代码 |
| 249 | `01_技术与AI / 编程开发与开源` | 前端开发规范 | 2 | `前端与 Web 开发` | `small:frontend-standards` | 阿里巴巴前端开发规范英文版文档；阿里巴巴前端开发规范中文版本文档 |
| 250 | `01_技术与AI / 编程开发与开源` | 前端技术博客 | 2 | `前端与 Web 开发` | `small:frontend-blogs` | OBKoro1前端进阶积累网站作者与内容介绍页面；海岛心hey前端工程师个人技术博客主页 |
| 251 | `01_技术与AI / 编程开发与开源` | 前端构建工具 | 2 | `前端与 Web 开发` | `small:frontend-build-tools` | webpack中文文档中的sass-loader配置指南；Vite快速开始指南与社区项目模板入口 |
| 252 | `01_技术与AI / 编程开发与开源` | 前端架构与微前端 | 1 | `前端与 Web 开发` | `small:frontend-architecture` | MicroApp 京东开源微前端应用框架文档 |
| 253 | `01_技术与AI / 编程开发与开源` | 前端组件与资源 | 2 | `前端与 Web 开发` | `small:frontend-resources` | Uiverse 免费 CSS 与 Tailwind UI 组件资源库；Tailwind Awesome 免费 Tailwind UI 套件资源筛选页 |
| 254 | `01_技术与AI / 编程开发与开源` | 前端组件文档 | 1 | `前端与 Web 开发` | `small:frontend-docs` | EmbedPDF 可嵌入 PDF 阅读器组件开发文档 |
| 255 | `01_技术与AI / 编程开发与开源` | Java中间件 | 2 | `后端、语言与服务端开发` | `small:java-middleware` | JBoss Application Server应用服务器官方下载页面；JBoss与WildFly目录部署失败问题论坛讨论 |
| 256 | `01_技术与AI / 编程开发与开源` | Java数据访问框架 | 2 | `后端、语言与服务端开发` | `small:java-data-access` | MyBatis-Plus Java 数据库持久层增强框架；Brucege 博客中的 MyBatis 插件付费页面 |
| 257 | `01_技术与AI / 编程开发与开源` | Node.js开发 | 1 | `后端、语言与服务端开发` | `small:nodejs-development` | dotenv环境变量配置Node.js软件包说明 |
| 258 | `01_技术与AI / 编程开发与开源` | Python 工具与发行版 | 1 | `后端、语言与服务端开发` | `small:python-tools` | Anaconda Python 发行版历史版本安装包下载目录 |
| 259 | `01_技术与AI / 编程开发与开源` | Python开发环境 | 1 | `后端、语言与服务端开发` | `small:python-environments` | Anaconda个人版Python数据科学环境官方下载页面 |
| 260 | `01_技术与AI / 编程开发与开源` | Spring Boot开发 | 1 | `后端、语言与服务端开发` | `small:spring-boot` | Spring Boot 与 IDEA 实现代码热部署教程 |
| 261 | `01_技术与AI / 编程开发与开源` | 后端开发平台 | 2 | `后端、语言与服务端开发` | `small:backend-platforms` | Supabase：开源 Firebase 替代方案与后端服务平台；Supabase demo 项目后台控制台与数据库管理 |
| 262 | `01_技术与AI / 编程开发与开源` | 后端开源项目 | 1 | `后端、语言与服务端开发` | `small:backend-projects` | FEBS开源后台用户与权限管理系统演示 |
| 263 | `01_技术与AI / 编程开发与开源` | 数据库开发 | 2 | `后端、语言与服务端开发` | `small:database-development` | ioredis Node.js Redis 客户端完整 API 文档；ClickHouse Playground 在线 SQL 查询与分析演示环境 |
| 264 | `01_技术与AI / 编程开发与开源` | Notion 建站工具 | 1 | `建站、CMS 与技术写作` | `small:notion-publishing` | NotionNext 将 Notion 笔记快速发布为网站的建站工具 |
| 265 | `01_技术与AI / 编程开发与开源` | 内容管理与建站 | 1 | `建站、CMS 与技术写作` | `small:content-management` | WordPress 官方开源博客发布平台与内容管理系统 |
| 266 | `01_技术与AI / 编程开发与开源` | 开发者学习路线 | 2 | `开发学习与职业成长` | `small:developer-learning` | roadmap.sh开发者学习路线图平台；计算机教育中缺失的一课命令行开发工具中文教程 |
| 267 | `01_技术与AI / 编程开发与开源` | 数据库学习 | 2 | `开发学习与职业成长` | `small:database-learning` | W3School 中文 SQL 数据库查询教程；DB-TUTORIAL数据库基础、SQL与系统原理学习教程 |
| 268 | `01_技术与AI / 编程开发与开源` | Android开发 | 1 | `开发工具、环境与工程实践` | `small:android-development` | Android SDK Platform Tools 平台工具版本说明 |
| 269 | `01_技术与AI / 编程开发与开源` | Qt 开发框架 | 1 | `开发工具、环境与工程实践` | `small:qt-development` | Qt 中文官网：跨平台嵌入式与桌面应用开发框架 |
| 270 | `01_技术与AI / 编程开发与开源` | 二维码工具 | 1 | `开发工具、环境与工程实践` | `small:qr-code-tools` | 草料二维码图片转二维码在线生成工具 |
| 271 | `01_技术与AI / 编程开发与开源` | 代码生成工具 | 2 | `开发工具、环境与工程实践` | `small:code-generators` | CodeFun 将 UI 设计稿智能转换为前端源代码；Easy Code 面向业务系统的在线代码生成器 |
| 272 | `01_技术与AI / 编程开发与开源` | 低代码开发平台 | 1 | `开发工具、环境与工程实践` | `small:low-code` | 美乐低代码企业数字化应用开发平台 |
| 273 | `01_技术与AI / 编程开发与开源` | 低代码开发平台 | 2 | `开发工具、环境与工程实践` | `small:low-code-development` | FormCreate开源低代码表单设计器在线演示；JEECG低代码开发平台官方演示账号页面 |
| 274 | `01_技术与AI / 编程开发与开源` | 区块链开发社区 | 1 | `开发工具、环境与工程实践` | `small:blockchain-development` | 登链社区区块链开发技术交流与学习平台 |
| 275 | `01_技术与AI / 编程开发与开源` | 在线开发工具 | 1 | `开发工具、环境与工程实践` | `small:online-developer-tools` | 在线UUID生成器用于创建唯一标识符 |
| 276 | `01_技术与AI / 编程开发与开源` | 小程序开发 | 1 | `开发工具、环境与工程实践` | `small:mini-program-development` | Taro 小程序主包优化编译配置参数详细说明 |
| 277 | `01_技术与AI / 编程开发与开源` | 开发协作平台 | 2 | `开发工具、环境与工程实践` | `small:developer-collaboration` | junw555的JetBrains Space团队协作空间主页；RabbitMQ 官方 Git 与 GitHub 协作贡献指南 |
| 278 | `01_技术与AI / 编程开发与开源` | 开发平台与 IDE | 1 | `开发工具、环境与工程实践` | `small:developer-platforms` | OpenSumi开源IDE框架与可扩展开发平台中文站 |
| 279 | `01_技术与AI / 编程开发与开源` | 开发平台账户管理 | 1 | `开发工具、环境与工程实践` | `small:developer-platform-accounts` | Vercel 账户设置与个人账号管理页面 |
| 280 | `01_技术与AI / 编程开发与开源` | 开发文档与知识库 | 1 | `开发工具、环境与工程实践` | `small:developer-docs` | Hao-Docs 技术知识库与开发文档站点 |
| 281 | `01_技术与AI / 编程开发与开源` | 开发环境与包管理 | 1 | `开发工具、环境与工程实践` | `small:package-managers` | Homebrew macOS与Linux软件包管理器官网 |
| 282 | `01_技术与AI / 编程开发与开源` | 开发环境配置 | 2 | `开发工具、环境与工程实践` | `small:development-environments` | Omakub：DHH 为 Ubuntu 24.04 提供的开发环境配置；vfox 多语言开发环境版本管理器快速入门 |
| 283 | `01_技术与AI / 编程开发与开源` | 开发者主页与作品集 | 1 | `开发工具、环境与工程实践` | `small:developer-portfolios` | Joohnsmith 个人网站或开发者作品主页 |
| 284 | `01_技术与AI / 编程开发与开源` | 开发者周边商品 | 1 | `开发工具、环境与工程实践` | `small:developer-merchandise` | Small Sticker 程序员主题贴纸在线商店 |
| 285 | `01_技术与AI / 编程开发与开源` | 开发者工具讨论 | 1 | `开发工具、环境与工程实践` | `small:developer-discussions` | Hacker News讨论易用的网站页面截图生成工具 |
| 286 | `01_技术与AI / 编程开发与开源` | 开发者搜索工具 | 2 | `开发工具、环境与工程实践` | `small:developer-search` | Goobe 面向程序员的技术资源互联网搜索引擎；Phind 面向开发者的人工智能技术问答搜索引擎 |
| 287 | `01_技术与AI / 编程开发与开源` | 开发者站点 | 1 | `开发工具、环境与工程实践` | `small:developer-sites` | FuryIO 开源软件与开发者服务站点 |
| 288 | `01_技术与AI / 编程开发与开源` | 开发者终端工具 | 1 | `开发工具、环境与工程实践` | `small:developer-cli-tools` | cmux 面向多任务工作的现代化终端应用 |
| 289 | `01_技术与AI / 编程开发与开源` | 开发者资源导航 | 2 | `开发工具、环境与工程实践` | `small:developer-resources` | Free for Developers 开发者免费服务资源导航；程序员哈皮个人资源与技术内容分享网页 |
| 290 | `01_技术与AI / 编程开发与开源` | 搜索服务开发 | 1 | `开发工具、环境与工程实践` | `small:search-development` | Meilisearch API 密钥创建与访问权限配置参考文档 |
| 291 | `01_技术与AI / 编程开发与开源` | 数据库与工程实践 | 2 | `开发工具、环境与工程实践` | `small:database-engineering` | GitHub 工程团队升级 GitHub.com 至 MySQL 8.0 的实践；使用PostgreSQL简化整体技术栈的实践文章 |
| 292 | `01_技术与AI / 编程开发与开源` | 数据库与搜索技术 | 2 | `开发工具、环境与工程实践` | `small:databases-and-search` | KeyDB高性能Redis兼容数据库官方文档；Manticore Search全文搜索引擎安装指南 |
| 293 | `01_技术与AI / 编程开发与开源` | 文字与编码工具 | 1 | `开发工具、环境与工程实践` | `small:text-and-encoding-tools` | 与熊论道熊曰文字加密与解密在线工具 |
| 294 | `01_技术与AI / 编程开发与开源` | 桌面应用开发 | 1 | `开发工具、环境与工程实践` | `small:desktop-development` | Tauri 桌面应用开发的环境依赖与前置要求文档 |
| 295 | `01_技术与AI / 编程开发与开源` | 消费电子产品数据库 | 1 | `开发工具、环境与工程实践` | `small:consumer-product-databases` | ChargeBaby：移动电源参数与型号查询数据库 |
| 296 | `01_技术与AI / 编程开发与开源` | 物联网开发 | 1 | `开发工具、环境与工程实践` | `small:iot-development` | 萤石开放平台 Android SDK 功能介绍文档 |
| 297 | `01_技术与AI / 编程开发与开源` | 独立开发与创业社区 | 1 | `开发工具、环境与工程实践` | `small:indie-development` | 独立开发者关于自我成长的社区讨论帖 |
| 298 | `01_技术与AI / 编程开发与开源` | 独立开发与用户增长 | 1 | `开发工具、环境与工程实践` | `small:indie-growth` | Indie Hacker 前一千名用户获客渠道指南 |
| 299 | `01_技术与AI / 编程开发与开源` | 独立开发者资源 | 1 | `开发工具、环境与工程实践` | `small:indie-developer-resources` | 独立开发者出海所需技术栈与实用工具导航 |
| 300 | `01_技术与AI / 编程开发与开源` | 电商开放平台 | 1 | `开发工具、环境与工程实践` | `small:ecommerce-development` | 阿里巴巴1688开放平台开发者服务入口 |
| 301 | `01_技术与AI / 编程开发与开源` | 研发协作工具 | 1 | `开发工具、环境与工程实践` | `small:software-development-tools` | TAPD一站式DevOps研发协作与项目管理方案 |
| 302 | `01_技术与AI / 编程开发与开源` | 算法与编程练习 | 2 | `开发工具、环境与工程实践` | `small:programming-practice` | 力扣LeetCode中文算法题库与编程学习平台；AmosCloud德科机试一星题编程练习题库 |
| 303 | `01_技术与AI / 编程开发与开源` | 编程竞赛与在线评测 | 1 | `开发工具、环境与工程实践` | `small:programming-contests` | QDUOJ在线评测系统中的ACM竞赛排名页面 |
| 304 | `01_技术与AI / 编程开发与开源` | 远程开发工作 | 1 | `开发工具、环境与工程实践` | `small:remote-jobs` | JS Remotely远程JavaScript开发工作资源平台 |
| 305 | `01_技术与AI / 编程开发与开源` | API与开发工具 | 1 | `开发文档、接口与参考` | `small:api-tools` | OpenList访问令牌获取与管理在线工具 |
| 306 | `01_技术与AI / 编程开发与开源` | 创投融资数据库 | 1 | `开发文档、接口与参考` | `small:venture-capital-databases` | IT桔子中国创业公司与投融资数据服务平台 |
| 307 | `01_技术与AI / 编程开发与开源` | 定位与地图 API | 1 | `开发文档、接口与参考` | `small:location-apis` | 基站WiFi与LBS经纬度定位数据查询接口 |
| 308 | `01_技术与AI / 编程开发与开源` | 开发者 API 服务 | 2 | `开发文档、接口与参考` | `small:developer-apis` | 和风天气开发者控制台应用配置与密钥管理；天行数据 TianAPI 开发者数据接口平台 |
| 309 | `01_技术与AI / 编程开发与开源` | 开发速查资料 | 2 | `开发文档、接口与参考` | `small:developer-reference` | 前端开发常用命令与工具快速参考手册；QuickRef多种编程语言与开发工具速查表集合 |
| 310 | `01_技术与AI / 编程开发与开源` | 水草造景 | 1 | `开发文档、接口与参考` | `small:aquascaping` | IAPLC 世界水草造景大赛官方作品平台 |
| 311 | `01_技术与AI / 编程开发与开源` | 编程知识参考 | 1 | `开发文档、接口与参考` | `small:programming-reference` | 编程词典在线开发术语与技术知识查询工具 |
| 312 | `01_技术与AI / 编程开发与开源` | Git 与版本控制工具 | 2 | `开源项目与版本协作` | `small:git-tools` | GitButler 面向开发者的 Git 分支与版本管理工具；Tower面向Mac与Windows的Git图形客户端 |
| 313 | `01_技术与AI / 编程开发与开源` | GitHub与开发者成长 | 1 | `开源项目与版本协作` | `small:github-and-career` | 使用README美化GitHub个人主页的配置教程 |
| 314 | `01_技术与AI / 编程开发与开源` | Git与版本控制 | 2 | `开源项目与版本协作` | `small:git-and-version-control` | 交互式学习 Git 分支操作与提交历史可视化教程；廖雪峰 Git 教程中的创建与合并分支操作指南 |
| 315 | `01_技术与AI / 编程开发与开源` | 代码版本管理 | 2 | `开源项目与版本协作` | `small:source-control` | 至恒融兴内部GitLab代码仓库登录入口；SourceTree跨平台Git图形化客户端官网 |
| 316 | `01_技术与AI / 编程开发与开源` | 企业开源项目 | 2 | `开源项目与版本协作` | `small:enterprise-open-source` | 人人开源社区的企业应用开发项目与技术资源；人人微服务 Renren Cloud 企业后台演示平台 |
| 317 | `01_技术与AI / 编程开发与开源` | 开发效率工具 | 2 | `开源项目与版本协作` | `small:developer-productivity` | WakaTime 开发编码时长与项目统计面板；Astral GitHub Stars 收藏项目整理工具控制台 |
| 318 | `01_技术与AI / 编程开发与开源` | 开发者数据与年度报告 | 1 | `开源项目与版本协作` | `small:developer-analytics` | YunYouJun 的 GitHub 年度趋势与开发活动总结 |
| 319 | `01_技术与AI / 编程开发与开源` | 开发者桌面工具 | 2 | `开源项目与版本协作` | `small:developer-desktop-tools` | FileZilla跨平台FTP文件传输客户端完整下载页；Fork 跨平台快速友好的 Git 图形客户端 |
| 320 | `01_技术与AI / 编程开发与开源` | 开发者活动 | 1 | `开源项目与版本协作` | `small:developer-events` | GitHub Universe 2022活动报名确认页面 |
| 321 | `01_技术与AI / 编程开发与开源` | 开源协议与许可证 | 1 | `开源项目与版本协作` | `small:open-source-licensing` | 开源软件许可证选择原则与常见协议对比讨论 |
| 322 | `01_技术与AI / 编程开发与开源` | 开源应用项目 | 1 | `开源项目与版本协作` | `small:open-source-applications` | Halo 开源 Java 博客与内容管理系统官方网站 |
| 323 | `01_技术与AI / 编程开发与开源` | 开源生态分析 | 1 | `开源项目与版本协作` | `small:open-source-analytics` | OSS Insight GitHub 开源项目趋势分析平台 |
| 324 | `01_技术与AI / 编程开发与开源` | 开源社区 | 2 | `开源项目与版本协作` | `small:open-source-communities` | Open GitHub 社区开源项目交流与资源平台；Fresns v2.1.0 正式版发布公告与更新说明 |
| 325 | `01_技术与AI / 编程开发与开源` | 开源项目与开发资讯 | 1 | `开源项目与版本协作` | `small:open-source-and-dev-news` | Open Source Daily开源项目与开发者资讯每日聚合站 |
| 326 | `01_技术与AI / 编程开发与开源` | 开源项目与计划 | 1 | `开源项目与版本协作` | `small:open-source-programs` | Google Summer of Code谷歌开源之夏项目官网 |
| 327 | `01_技术与AI / 编程开发与开源` | 开源项目与软件 | 1 | `开源项目与版本协作` | `small:open-source-projects` | TORCS开源赛车模拟器项目新闻与软件下载页面 |
| 328 | `01_技术与AI / 编程开发与开源` | 开源项目发现 | 1 | `开源项目与版本协作` | `small:open-source-discovery` | Rising Repo筛选GitHub近期热门上升项目工具 |
| 329 | `01_技术与AI / 编程开发与开源` | 数字商品与账号服务 | 2 | `开源项目与版本协作` | `small:digital-goods` | 美区 Apple ID 与 Shadowrocket 兑换码购买网站汇总；Netflix 会员账号与海外 Apple ID 自动发卡页面 |
| 330 | `01_技术与AI / 编程开发与开源` | 数字档案与网页存档 | 1 | `开源项目与版本协作` | `small:digital-archives` | Internet Archive免费数字图书影音与网页存档馆 |
| 331 | `01_技术与AI / 编程开发与开源` | 数字游民与城市比较 | 1 | `开源项目与版本协作` | `small:digital-nomad-tools` | Nomad List全球数字游民城市生活成本比较平台 |
| 332 | `01_技术与AI / 编程开发与开源` | 数字游民与海外生活 | 2 | `开源项目与版本协作` | `small:digital-nomads` | RW实验室数字游民社区与远程工作平台；TangTalk数字居民服务与海外生活咨询网站 |
| 333 | `01_技术与AI / 编程开发与开源` | 数字金融服务 | 2 | `开源项目与版本协作` | `small:digital-finance` | Fiat24代币化支付与数字金融服务平台；Fiat24个人账户资产与支付管理仪表盘 |
| 334 | `02_学习与研究 / 论文、科研与学科资料` | 旅行与地理内容 | 2 | `人文、社会与区域研究` | `small:travel-and-geography` | 中国国家地理网深度旅行与自然地理内容平台；穷游论坛叙利亚伊拉克与罗贾瓦深度旅行纪实帖 |
| 335 | `02_学习与研究 / 论文、科研与学科资料` | 健康与医学 | 2 | `医学、生物与生命科学` | `small:health-medicine` | 医学微视权威医学专家健康科普视频平台；默沙东诊疗手册中文版权威医学健康知识库 |
| 336 | `02_学习与研究 / 论文、科研与学科资料` | 健康与医疗技术 | 1 | `医学、生物与生命科学` | `small:health-and-medical` | Nightscout开源糖尿病血糖监测项目介绍 |
| 337 | `02_学习与研究 / 论文、科研与学科资料` | 医疗机构与医生 | 1 | `医学、生物与生命科学` | `small:medical-profiles` | 中山大学附属第七医院张影医生个人页面 |
| 338 | `02_学习与研究 / 论文、科研与学科资料` | 医美医生信息 | 1 | `医学、生物与生命科学` | `small:medical-aesthetics` | 新氧平台任东医生医美项目与评价信息 |
| 339 | `02_学习与研究 / 论文、科研与学科资料` | 家族祖源基因分析 | 2 | `医学、生物与生命科学` | `small:genealogy-dna` | 福建泉州晋江蔡氏祖源基因分析结果页面；湖南邵阳刘氏祖源基因分析结果页面 |
| 340 | `02_学习与研究 / 论文、科研与学科资料` | 生物学学习资料 | 1 | `医学、生物与生命科学` | `small:biology-study` | 玉米籽粒颜色遗传与自由授粉群体题目解析 |
| 341 | `02_学习与研究 / 论文、科研与学科资料` | 生物学查询与参考 | 1 | `医学、生物与生命科学` | `small:biology-reference` | CFH中国生物物种名称综合在线查询 |
| 342 | `02_学习与研究 / 论文、科研与学科资料` | 药品医疗器械监管 | 2 | `医学、生物与生命科学` | `small:medical-regulation` | 国家药品监督管理局药品医疗器械监管官方网站；国家药监局国产医疗器械产品注册数据查询 |
| 343 | `02_学习与研究 / 论文、科研与学科资料` | 人工智能学术组织 | 1 | `学科资料与研究参考` | `small:artificial-intelligence` | 中国人工智能学会官方网站与行业资讯 |
| 344 | `02_学习与研究 / 论文、科研与学科资料` | 人格与心理测试 | 1 | `学科资料与研究参考` | `small:personality-tests` | 16Personalities免费人格类型与性格测试 |
| 345 | `02_学习与研究 / 论文、科研与学科资料` | 创意互动实验 | 2 | `学科资料与研究参考` | `small:creative-interactive` | BLACKOUT随音乐节奏变化的互动视听体验；Neal.fun设计下一代iPhone的互动网页体验 |
| 346 | `02_学习与研究 / 论文、科研与学科资料` | 古籍与历史文献 | 1 | `学科资料与研究参考` | `small:rare-books` | 书格中国古籍高清影像与文献搜索 |
| 347 | `02_学习与研究 / 论文、科研与学科资料` | 咨询研究资源 | 1 | `学科资料与研究参考` | `small:consulting-resources` | 咨询圈工作室共享咨询与行业研究资源网站 |
| 348 | `02_学习与研究 / 论文、科研与学科资料` | 商业管理研究 | 1 | `学科资料与研究参考` | `small:business-management` | 沃顿知识在线商业管理思想与研究文章平台 |
| 349 | `02_学习与研究 / 论文、科研与学科资料` | 地理空间与遥感 | 2 | `学科资料与研究参考` | `small:geospatial-tools` | 51Earth 地球观测与遥感应用服务平台；51Earth 平台服务申请与接入页面 |
| 350 | `02_学习与研究 / 论文、科研与学科资料` | 学术英语与写作 | 1 | `学科资料与研究参考` | `small:english-writing` | OpenLearning学术英语写作在线课程 |
| 351 | `02_学习与研究 / 论文、科研与学科资料` | 心理学与人格 | 1 | `学科资料与研究参考` | `small:psychology-and-personality` | MBTI十六型人格理论入门与性格类型介绍 |
| 352 | `02_学习与研究 / 论文、科研与学科资料` | 敏感心理健康内容 | 1 | `学科资料与研究参考` | `small:sensitive-mental-health` | 含自杀相关主题的个人博客文章页面 |
| 353 | `02_学习与研究 / 论文、科研与学科资料` | 数据科学计算 | 2 | `学科资料与研究参考` | `small:data-science` | Project Jupyter 交互式数据科学计算平台；机器学习数据缺失混乱重复问题的数据清洗指南 |
| 354 | `02_学习与研究 / 论文、科研与学科资料` | 汉字研究与字形资料 | 1 | `学科资料与研究参考` | `small:chinese-character-resources` | 北京师范大学汉字全息资源应用系统入口 |
| 355 | `02_学习与研究 / 论文、科研与学科资料` | 科学传播与媒体制作 | 1 | `学科资料与研究参考` | `small:science-media` | SciePro科学影视制作与科普内容服务 |
| 356 | `02_学习与研究 / 论文、科研与学科资料` | 科学研究资讯 | 1 | `学科资料与研究参考` | `small:science-news` | ScienceDaily全球科研新闻与科学资讯 |
| 357 | `02_学习与研究 / 论文、科研与学科资料` | 科技商业资讯与研究 | 1 | `学科资料与研究参考` | `small:tech-business-news` | 钛媒体科技商业研究报告与产业创新资讯 |
| 358 | `02_学习与研究 / 论文、科研与学科资料` | 科技科普资讯 | 2 | `学科资料与研究参考` | `small:science-technology-media` | 果壳网泛科技科普内容与交流社区；科学松鼠会面向大众的科学传播网站 |
| 359 | `02_学习与研究 / 论文、科研与学科资料` | 科普与知识文章 | 1 | `学科资料与研究参考` | `small:science-explainers` | HowStuffWorks万物工作原理科普知识网站 |
| 360 | `02_学习与研究 / 论文、科研与学科资料` | 管理研究报告与文档 | 1 | `学科资料与研究参考` | `small:management-reports` | MBA智库文档商业管理研究报告与知识资料库 |
| 361 | `02_学习与研究 / 论文、科研与学科资料` | 计算机科学项目 | 1 | `学科资料与研究参考` | `small:computer-science-programs` | KAUST计算机科学项目课程研究与招生信息 |
| 362 | `02_学习与研究 / 论文、科研与学科资料` | 论文查重检测 | 2 | `学科资料与研究参考` | `small:plagiarism-checking` | PaperPass学术论文重复率检测平台；CNKI知网论文重复率检测服务入口 |
| 363 | `02_学习与研究 / 论文、科研与学科资料` | 办公空间服务 | 1 | `理工、工程与自然科学` | `small:workspace-services` | WeWork 联合办公空间与会议室预订服务 |
| 364 | `02_学习与研究 / 论文、科研与学科资料` | 宇宙与太空模拟 | 1 | `理工、工程与自然科学` | `small:space-simulation` | SpaceEngine 宇宙空间模拟器软件下载与介绍页面 |
| 365 | `02_学习与研究 / 论文、科研与学科资料` | 数学与工程软件 | 2 | `理工、工程与自然科学` | `small:math-and-engineering` | MathWorks在线安装MATLAB与Simulink产品指南；MathWorks账户登录与MATLAB软件下载入口 |
| 366 | `02_学习与研究 / 论文、科研与学科资料` | 数学与集合论讨论 | 1 | `理工、工程与自然科学` | `small:mathematics-discussions` | 自然数函数集合不可数性的集合论证明讨论 |
| 367 | `02_学习与研究 / 论文、科研与学科资料` | 数学公式工具 | 1 | `理工、工程与自然科学` | `small:math-tools` | LaTeX Live 在线数学公式编辑与预览工具 |
| 368 | `02_学习与研究 / 论文、科研与学科资料` | 数学学习资料 | 2 | `理工、工程与自然科学` | `small:math-learning` | 玄数网站三角函数与反三角函数图像教程；玄数网站三角函数基础概念与图像教程 |
| 369 | `02_学习与研究 / 论文、科研与学科资料` | 文档与公式工具 | 2 | `理工、工程与自然科学` | `small:document-and-math-tools` | MathType数学公式编辑器中文官网与使用教程；Mathpix Snipping Tool数学公式识别工具下载页面 |
| 370 | `02_学习与研究 / 论文、科研与学科资料` | AI 研究与演示 | 1 | `论文、文献与科研工具` | `small:ai-research` | NVIDIA Research在线深度学习交互演示平台 |
| 371 | `02_学习与研究 / 论文、科研与学科资料` | AI 研究项目 | 2 | `论文、文献与科研工具` | `small:ai-research-projects` | Abel生成式人工智能数学推理能力研究项目；IDE-3D高分辨率人像交互式编辑项目 |
| 372 | `02_学习与研究 / 论文、科研与学科资料` | AI新闻与研究 | 1 | `论文、文献与科研工具` | `small:ai-news-research` | 上海人工智能实验室发布AI高考评测结果 |
| 373 | `02_学习与研究 / 论文、科研与学科资料` | SaaS 市场分析 | 1 | `论文、文献与科研工具` | `small:saas-market-research` | WhoisMaking Money 基于 Stripe 支付流量的营收分析工具 |
| 374 | `02_学习与研究 / 论文、科研与学科资料` | VPN 市场调研 | 1 | `论文、文献与科研工具` | `small:vpn-research` | 白鲸出海平台VPN应用市场搜索结果 |
| 375 | `02_学习与研究 / 论文、科研与学科资料` | 互联网数据研究 | 2 | `论文、文献与科研工具` | `small:internet-research` | 199IT中文互联网数据研究资讯中心首页；比达咨询互联网大数据行业研究报告平台 |
| 376 | `02_学习与研究 / 论文、科研与学科资料` | 人工智能研究机构 | 2 | `论文、文献与科研工具` | `small:ai-research-labs` | 马克斯普朗克智能系统研究所感知系统研究部门；Matthias Nießner实验室视觉计算与人工智能研究 |
| 377 | `02_学习与研究 / 论文、科研与学科资料` | 企业工商查询 | 1 | `论文、文献与科研工具` | `small:company-research` | 东莞市三基音响科技有限公司工商信用信息 |
| 378 | `02_学习与研究 / 论文、科研与学科资料` | 信息通信研究报告 | 1 | `论文、文献与科研工具` | `small:ict-research-reports` | 中国信通院科研能力权威发布白皮书页面 |
| 379 | `02_学习与研究 / 论文、科研与学科资料` | 创投与私募市场研究 | 2 | `论文、文献与科研工具` | `small:venture-capital-research` | 投中网中国创业投资私募股权行业资讯数据平台；清科研究中心私募股权与创投市场研究报告 |
| 380 | `02_学习与研究 / 论文、科研与学科资料` | 医疗健康行业研究 | 1 | `论文、文献与科研工具` | `small:healthcare-industry-research` | 动脉网医疗健康产业研究报告与创新资讯 |
| 381 | `02_学习与研究 / 论文、科研与学科资料` | 品牌研究与榜单 | 1 | `论文、文献与科研工具` | `small:brand-research` | Chnbrand BrandKey中国顾客满意度指数榜单 |
| 382 | `02_学习与研究 / 论文、科研与学科资料` | 商业数据与行业研究 | 1 | `论文、文献与科研工具` | `small:business-data-research` | 镝数聚商业数据可视化与行业研究服务平台 |
| 383 | `02_学习与研究 / 论文、科研与学科资料` | 大数据与营销研究 | 1 | `论文、文献与科研工具` | `small:data-marketing-research` | 国双数据中心数字营销与大数据研究报告 |
| 384 | `02_学习与研究 / 论文、科研与学科资料` | 学术 AI 搜索与问答 | 1 | `论文、文献与科研工具` | `small:academic-ai-search` | Consensus基于论文证据的智能问答搜索平台 |
| 385 | `02_学习与研究 / 论文、科研与学科资料` | 学术文献工具 | 2 | `论文、文献与科研工具` | `small:research-literature` | ReadPaper论文阅读与学术讨论社区平台；Connected Papers学术文献关系可视化探索工具 |
| 386 | `02_学习与研究 / 论文、科研与学科资料` | 学术职业发展 | 1 | `论文、文献与科研工具` | `small:academic-careers` | 蒂宾根大学博士后与青年研究人员职业发展机会 |
| 387 | `02_学习与研究 / 论文、科研与学科资料` | 新经济产业研究 | 1 | `论文、文献与科研工具` | `small:new-economy-research` | 亿欧智库新经济产业研究报告与商业资讯 |
| 388 | `02_学习与研究 / 论文、科研与学科资料` | 新能源与气候研究 | 1 | `论文、文献与科研工具` | `small:energy-and-climate-research` | BloombergNEF全球新能源转型与气候金融研究机构 |
| 389 | `02_学习与研究 / 论文、科研与学科资料` | 旅游行业研究 | 1 | `论文、文献与科研工具` | `small:travel-industry-research` | 旅游圈旅游行业数据专题与市场研究资讯 |
| 390 | `02_学习与研究 / 论文、科研与学科资料` | 求职调研与薪资评价 | 1 | `论文、文献与科研工具` | `small:job-research` | Glassdoor公司薪资评价与职位搜索平台 |
| 391 | `02_学习与研究 / 论文、科研与学科资料` | 汽车评测与研究 | 1 | `论文、文献与科研工具` | `small:automotive-research` | 中国保险汽车安全指数车型碰撞测试结果查询 |
| 392 | `02_学习与研究 / 论文、科研与学科资料` | 消费者与市场研究 | 1 | `论文、文献与科研工具` | `small:consumer-market-research` | 尼尔森中国消费者洞察与市场测量研究服务 |
| 393 | `02_学习与研究 / 论文、科研与学科资料` | 用户研究与可用性测试 | 1 | `论文、文献与科研工具` | `small:user-research` | UserCrowd用户测试平台手机号码验证页面 |
| 394 | `02_学习与研究 / 论文、科研与学科资料` | 电子书与学术资料 | 1 | `论文、文献与科研工具` | `small:ebooks-and-research` | Library Genesis电子书与学术资料检索站 |
| 395 | `02_学习与研究 / 论文、科研与学科资料` | 研究与信息检索 | 1 | `论文、文献与科研工具` | `small:research-tools` | Tavily Company Research 企业信息检索研究工具 |
| 396 | `02_学习与研究 / 论文、科研与学科资料` | 研究项目与实验室 | 2 | `论文、文献与科研工具` | `small:research-labs` | 维尔茨堡大学法学院CDSV研究项目团队成员；KAUST生物本体研究组研究方向与团队信息 |
| 397 | `02_学习与研究 / 论文、科研与学科资料` | 科研创新机构 | 2 | `论文、文献与科研工具` | `small:research-and-innovation` | 山东产业技术研究院烟台创新研发服务平台；中国管理科学研究院行业发展研究所官方网站 |
| 398 | `02_学习与研究 / 论文、科研与学科资料` | 科研实验室服务 | 1 | `论文、文献与科研工具` | `small:research-supplies` | 探索平台科研试剂耗材仪器与实验室服务 |
| 399 | `02_学习与研究 / 论文、科研与学科资料` | 科研机构排名 | 1 | `论文、文献与科研工具` | `small:research-rankings` | Nature Index亚太地区2021年度科研机构排名 |
| 400 | `02_学习与研究 / 论文、科研与学科资料` | 科研知识资源 | 1 | `论文、文献与科研工具` | `small:research-knowledge` | MiniManuscript 维基式学术论文与研究知识百科 |
| 401 | `02_学习与研究 / 论文、科研与学科资料` | 科研资讯 | 2 | `论文、文献与科研工具` | `small:research-news` | 搜学术学术圈科研热点与学界新闻聚合；中国学术会议在线会议论文与会议信息平台 |
| 402 | `02_学习与研究 / 论文、科研与学科资料` | 科研项目申报 | 1 | `论文、文献与科研工具` | `small:research-funding` | 全国哲学社会科学工作办公室项目申报与成果管理官网 |
| 403 | `02_学习与研究 / 论文、科研与学科资料` | 移动互联网数据研究 | 2 | `论文、文献与科研工具` | `small:mobile-data-research` | Trustdata移动互联网数据洞察与行业研究报告；极光移动应用数据研究报告与用户行为洞察 |
| 404 | `02_学习与研究 / 论文、科研与学科资料` | 移动应用安全研究 | 1 | `论文、文献与科研工具` | `small:mobile-security-research` | 安卓微信聊天信息本地存储机制解析文章 |
| 405 | `02_学习与研究 / 论文、科研与学科资料` | 经济研究与资讯 | 1 | `论文、文献与科研工具` | `small:economics-research` | 中国人民大学中国宏观经济论坛 CMF 官方网站 |
| 406 | `02_学习与研究 / 论文、科研与学科资料` | 经济研究机构 | 1 | `论文、文献与科研工具` | `small:economic-research-institutes` | 北京方迪经济发展研究院资讯与研究成果页面 |
| 407 | `02_学习与研究 / 论文、科研与学科资料` | 网络审查研究 | 2 | `论文、文献与科研工具` | `small:internet-censorship-research` | 积至与MESA实验室文件泄露深度分析报告；中国区域性防火墙相关泄露分析章节 |
| 408 | `02_学习与研究 / 论文、科研与学科资料` | 苹果设备参数查询 | 2 | `论文、文献与科研工具` | `small:apple-device-research` | HubWeb苹果产品参数与规格查询中心；MacRumors苹果设备购买时机与换代周期指南 |
| 409 | `02_学习与研究 / 论文、科研与学科资料` | 行业与市场研究 | 1 | `论文、文献与科研工具` | `small:market-research` | 中国产业信息网行业前景与投资趋势研究报告 |
| 410 | `02_学习与研究 / 论文、科研与学科资料` | 计算机科研团队 | 1 | `论文、文献与科研工具` | `small:computer-research` | 南京大学PASCAL教学与程序分析研究团队主页 |
| 411 | `02_学习与研究 / 论文、科研与学科资料` | 金融研究工具 | 1 | `论文、文献与科研工具` | `small:finance-research-tools` | 研报问小白面向投资研究报告的专业AI搜索工具 |
| 412 | `02_学习与研究 / 论文、科研与学科资料` | 食品产业研究 | 1 | `论文、文献与科研工具` | `small:food-industry-research` | 湖南省小龙虾市场全景调查与发展前景预测报告 |
| 413 | `02_学习与研究 / 阅读、语言与通识学习` | AI 阅读工具 | 1 | `书籍、阅读与文学` | `small:ai-reading` | ChatBook AI 图书对话阅读与智能问答产品概览 |
| 414 | `02_学习与研究 / 阅读、语言与通识学习` | PDF 与电子书工具 | 2 | `书籍、阅读与文学` | `small:pdf-and-ebooks` | PDF Drive在线PDF电子书搜索下载平台；PDFescape免费在线PDF编辑与表单填写工具 |
| 415 | `02_学习与研究 / 阅读、语言与通识学习` | 书签与搜索导航 | 2 | `书籍、阅读与文学` | `small:bookmark-and-search` | Raindrop.io 个人书签收藏与分类管理平台；虫部落聚合搜索引擎与资源检索导航 |
| 416 | `02_学习与研究 / 阅读、语言与通识学习` | 书签与网址导航 | 1 | `书籍、阅读与文学` | `small:bookmark-discovery` | 书签地球浏览器书签共享与发现平台 |
| 417 | `02_学习与研究 / 阅读、语言与通识学习` | 书评与读书资源 | 1 | `书籍、阅读与文学` | `small:book-review-resources` | 读写人书评杂志博客与读书资源聚合平台 |
| 418 | `02_学习与研究 / 阅读、语言与通识学习` | 文学作品资源 | 1 | `书籍、阅读与文学` | `small:literature-resources` | 诺贝尔文学奖作品PDF文件访问分享页面 |
| 419 | `02_学习与研究 / 阅读、语言与通识学习` | 旅行预订 | 2 | `书籍、阅读与文学` | `small:travel-booking` | 携程旅行酒店机票预订与旅游度假服务；Booking.com 东京酒店住宿搜索与预订结果 |
| 420 | `02_学习与研究 / 阅读、语言与通识学习` | 电子书管理 | 1 | `书籍、阅读与文学` | `small:ebook-tools` | Calibre 开源电子书管理、转换与阅读软件官网 |
| 421 | `02_学习与研究 / 阅读、语言与通识学习` | 编程书籍与技术资源 | 2 | `书籍、阅读与文学` | `small:programming-books` | SaltTiger 每日编程技术书籍与学习资源分享；SaltTiger 网站收录的 Java 编程书籍资源 |
| 422 | `02_学习与研究 / 阅读、语言与通识学习` | 语言与阅读学习 | 2 | `书籍、阅读与文学` | `small:language-reading-learning` | Spreeder CX 在线快速外文阅读训练工具；多邻国趣味化免费英语语言学习平台 |
| 423 | `02_学习与研究 / 阅读、语言与通识学习` | 软件工程阅读 | 2 | `书籍、阅读与文学` | `small:software-engineering-reading` | 合格程序员推荐阅读书单与技术书籍指南；重构大师中文版常用软件设计模式图文指南 |
| 424 | `02_学习与研究 / 阅读、语言与通识学习` | 阅读与书签管理 | 1 | `书籍、阅读与文学` | `small:reading-and-bookmarks` | Omnivore 稍后阅读与网页内容收藏服务 |
| 425 | `02_学习与研究 / 阅读、语言与通识学习` | 阅读与书籍博客 | 1 | `书籍、阅读与文学` | `small:reading-blogs` | 恩京书房个人阅读与书籍分享网站 |
| 426 | `02_学习与研究 / 阅读、语言与通识学习` | 阅读写作社区 | 1 | `书籍、阅读与文学` | `small:reading-writing-communities` | 十五言高质量阅读写作与思想交流社区 |
| 427 | `02_学习与研究 / 阅读、语言与通识学习` | 传统文化与命理 | 1 | `文化、艺术与通识资料` | `small:traditional-culture` | 中国传统风水方位与命理在线测算工具 |
| 428 | `02_学习与研究 / 阅读、语言与通识学习` | 文化创意站点 | 1 | `文化、艺术与通识资料` | `small:culture-sites` | 大衆SHOCK堂独立文化与创意内容展示网站 |
| 429 | `02_学习与研究 / 阅读、语言与通识学习` | 日本文化 | 1 | `文化、艺术与通识资料` | `small:japanese-culture` | 刀剑世界日本刀历史知识与名刀资料网站 |
| 430 | `02_学习与研究 / 阅读、语言与通识学习` | 美食与饮食文化 | 1 | `文化、艺术与通识资料` | `small:food-culture` | TasteAtlas全球地方美食与食材百科检索 |
| 431 | `02_学习与研究 / 阅读、语言与通识学习` | 编程语言学习 | 1 | `语言、翻译与词典` | `small:programming-languages` | 网道 C 语言基本语法中文教程 |
| 432 | `02_学习与研究 / 阅读、语言与通识学习` | 编程语言环境 | 1 | `语言、翻译与词典` | `small:language-runtimes` | RubyInstaller Windows平台Ruby安装包下载页面 |
| 433 | `02_学习与研究 / 阅读、语言与通识学习` | 英语词典 | 1 | `语言、翻译与词典` | `small:english-dictionaries` | Merriam-Webster词典中nickname单词释义页面 |
| 434 | `02_学习与研究 / 阅读、语言与通识学习` | 语文与古诗文学习 | 1 | `语言、翻译与词典` | `small:chinese-language-learning` | 古诗文网高中阶段文言文原文翻译大全 |
| 435 | `02_学习与研究 / 阅读、语言与通识学习` | 语言学习与翻译 | 1 | `语言、翻译与词典` | `small:language-tools` | 爱粤语在线粤语翻译与学习辅助工具 |
| 436 | `02_学习与研究 / 阅读、语言与通识学习` | 语言能力测评 | 1 | `语言、翻译与词典` | `small:language-assessment` | 中文词汇量在线测试与语言能力评估 |
| 437 | `02_学习与研究 / 阅读、语言与通识学习` | 转录与翻译工具 | 1 | `语言、翻译与词典` | `small:transcription-translation` | Transcription Hub 在线翻译应用服务页面 |
| 438 | `02_学习与研究 / 阅读、语言与通识学习` | AI 学习资源 | 2 | `通识学习与参考资源` | `small:ai-resources` | 鱼皮AI知识库收录的DeepSeek开源项目；DeepSeek官方模型应用与工具整理指南 |
| 439 | `02_学习与研究 / 阅读、语言与通识学习` | AI 文档阅读与分析 | 1 | `通识学习与参考资源` | `small:ai-document-tools` | ChatDOC通过人工智能对话阅读分析文档工具 |
| 440 | `02_学习与研究 / 阅读、语言与通识学习` | AI学习资源 | 1 | `通识学习与参考资源` | `small:ai-learning-resources` | Anthropic 官方课程与 AI 产品学习平台 |
| 441 | `02_学习与研究 / 阅读、语言与通识学习` | Go语言开发 | 1 | `通识学习与参考资源` | `small:go-programming` | Go语言设计与实现：运行时、编译器和并发机制解析 |
| 442 | `02_学习与研究 / 阅读、语言与通识学习` | Mac使用教程 | 1 | `通识学习与参考资源` | `small:macos-tutorials` | 国行Mac启用Apple Intelligence功能的操作教程 |
| 443 | `02_学习与研究 / 阅读、语言与通识学习` | NAS自托管与部署教程 | 1 | `通识学习与参考资源` | `small:nas-self-hosting` | 飞牛fnOS通过Docker部署Immich人脸识别相册教程 |
| 444 | `02_学习与研究 / 阅读、语言与通识学习` | Notion教程与资源 | 1 | `通识学习与参考资源` | `small:notion-resources` | Notion进度条制作教程及三种样式公式示例 |
| 445 | `02_学习与研究 / 阅读、语言与通识学习` | Python爬虫教程 | 1 | `通识学习与参考资源` | `small:python-web-scraping` | Python爬取个人微博主页图片的一键追星教程 |
| 446 | `02_学习与研究 / 阅读、语言与通识学习` | Windows 软件与教程 | 2 | `通识学习与参考资源` | `small:windows-software-guides` | 2021年精选20款电脑装机必备软件资源包；Windows十破解应用商店付费应用教程 |
| 447 | `02_学习与研究 / 阅读、语言与通识学习` | 代理配置教程 | 2 | `通识学习与参考资源` | `small:proxy-configuration-guides` | 科学上网配置教程：VMess、WebSocket 与 CDN；免流网络混淆参数配置参考文章 |
| 448 | `02_学习与研究 / 阅读、语言与通识学习` | 公共图书馆资源 | 2 | `通识学习与参考资源` | `small:public-libraries` | 浙江图书馆馆藏资源与读者服务网站；广西壮族自治区图书馆资源服务网站 |
| 449 | `02_学习与研究 / 阅读、语言与通识学习` | 区块链测试网教程 | 2 | `通识学习与参考资源` | `small:blockchain-testnets` | Aptos激励测试网第三期零成本节点搭建教程；Sui测试网项目交互与激励参与操作教程 |
| 450 | `02_学习与研究 / 阅读、语言与通识学习` | 古籍与传统文化 | 1 | `通识学习与参考资源` | `small:chinese-classics` | 识典古籍：在线阅读与检索中国传统古籍文献平台 |
| 451 | `02_学习与研究 / 阅读、语言与通识学习` | 图书馆服务 | 1 | `通识学习与参考资源` | `small:library-services` | 浙江图书馆网借平台在线图书借阅服务入口 |
| 452 | `02_学习与研究 / 阅读、语言与通识学习` | 图书馆电子资源 | 1 | `通识学习与参考资源` | `small:library-resources` | 广西图书馆电子资源平台在线浏览入口 |
| 453 | `02_学习与研究 / 阅读、语言与通识学习` | 学习与练习工具 | 1 | `通识学习与参考资源` | `small:learning-and-practice` | Monkeytype 在线打字速度测试与键盘练习工具 |
| 454 | `02_学习与研究 / 阅读、语言与通识学习` | 学习平台服务 | 1 | `通识学习与参考资源` | `small:learning-services` | 科技强国学习平台辅助访问网站 |
| 455 | `02_学习与研究 / 阅读、语言与通识学习` | 数字图书与古籍 | 1 | `通识学习与参考资源` | `small:digital-library` | 书格在线古籍与艺术图书数字阅览资源网站 |
| 456 | `02_学习与研究 / 阅读、语言与通识学习` | 文化产业统计数据 | 1 | `通识学习与参考资源` | `small:cultural-industry-statistics` | 2023年前三季度全国文化产业企业营业收入统计数据 |
| 457 | `02_学习与研究 / 阅读、语言与通识学习` | 文化遗产与数字档案 | 2 | `通识学习与参考资源` | `small:cultural-archives` | 台北故宫博物院开放资料与数位典藏图像集；Europeana欧洲文化遗产数字图书馆与馆藏检索 |
| 458 | `02_学习与研究 / 阅读、语言与通识学习` | 本地化翻译工具 | 1 | `通识学习与参考资源` | `small:localization-tools` | Crowdin 团队协作本地化翻译管理平台 |
| 459 | `02_学习与研究 / 阅读、语言与通识学习` | 机器学习文档 | 2 | `通识学习与参考资源` | `small:machine-learning-docs` | TensorFlow在Windows系统上的安装配置教程；Keras优化器API参考文档与训练算法说明 |
| 460 | `02_学习与研究 / 阅读、语言与通识学习` | 电子阅读器与 Kindle 指南 | 2 | `通识学习与参考资源` | `small:ereader-guides` | 书伴Kindle阅读技巧电子书与设备使用指南；狗耳朵BBC内容推送至Kindle阅读服务 |
| 461 | `02_学习与研究 / 阅读、语言与通识学习` | 编程语言趋势 | 1 | `通识学习与参考资源` | `small:programming-trends` | TIOBE编程语言流行度指数排行榜 |
| 462 | `02_学习与研究 / 阅读、语言与通识学习` | 网络代理部署教程 | 1 | `通识学习与参考资源` | `small:network-proxy-guides` | Xray VPS 代理服务一键部署配置教程 |
| 463 | `02_学习与研究 / 阅读、语言与通识学习` | 自学资源导航 | 1 | `通识学习与参考资源` | `small:self-learning-resources` | 学吧导航综合在线自学资源网站入口 |
| 464 | `02_学习与研究 / 阅读、语言与通识学习` | 自然语言处理与AI | 1 | `通识学习与参考资源` | `small:nlp-and-ai` | JioNLP中文自然语言处理工具包的DeepWiki文档 |
| 465 | `02_学习与研究 / 阅读、语言与通识学习` | 虚拟文化遗产 | 2 | `通识学习与参考资源` | `small:virtual-heritage` | Mused古代遗址与博物馆虚拟游览平台；Mused虚拟游览埃及奈费尔塔里王后墓 |
| 466 | `02_学习与研究 / 高校、留学与考试` | ASU课程资料 | 2 | `学习课程与教育资源` | `small:asu-courses` | ASU FSE 598 新兴计算技术课程主页与导览；ASU FSE 501 技术创业课程在线阅读书单 |
| 467 | `02_学习与研究 / 高校、留学与考试` | GMAT考试与留学 | 2 | `学习课程与教育资源` | `small:gmat-preparation` | 申请方解读GMAT考试用途与适用人群；新东方GMAT考试基础介绍与备考说明 |
| 468 | `02_学习与研究 / 高校、留学与考试` | IT职业认证 | 1 | `学习课程与教育资源` | `small:it-certification` | CIW中国认证官方网站与认证项目导航 |
| 469 | `02_学习与研究 / 高校、留学与考试` | NFT学习课程 | 2 | `学习课程与教育资源` | `small:nft-learning` | 101 Blockchains NFT基础知识在线课程；Skillshare面向新手的NFT铸造与销售课程 |
| 470 | `02_学习与研究 / 高校、留学与考试` | 个人证书与定制服务 | 1 | `学习课程与教育资源` | `small:personal-services` | Michael Sutter 学历证书装裱服务登录页面 |
| 471 | `02_学习与研究 / 高校、留学与考试` | 人工智能博士项目 | 1 | `学习课程与教育资源` | `small:ai-doctoral-programs` | Science of Intelligence人工智能博士培养项目介绍 |
| 472 | `02_学习与研究 / 高校、留学与考试` | 博士职位与科研招聘 | 1 | `学习课程与教育资源` | `small:phd-jobs` | 于利希研究中心能源系统高性能计算博士职位招聘 |
| 473 | `02_学习与研究 / 高校、留学与考试` | 博士资助与奖学金 | 2 | `学习课程与教育资源` | `small:doctoral-funding` | 汉堡大学博士阶段资助渠道与研究经费说明；KAUST全额资助硕士博士奖学金与助学金说明 |
| 474 | `02_学习与研究 / 高校、留学与考试` | 国际教育交流 | 1 | `学习课程与教育资源` | `small:international-education` | 中国东盟教育交流周主题活动资讯平台 |
| 475 | `02_学习与研究 / 高校、留学与考试` | 在线教育课程 | 1 | `学习课程与教育资源` | `small:online-education` | Sophia Learning 提供大学学分在线课程学习平台 |
| 476 | `02_学习与研究 / 高校、留学与考试` | 在线课程制作工具 | 1 | `学习课程与教育资源` | `small:elearning-authoring` | iSpring Suite在线课程制作软件免费试用下载 |
| 477 | `02_学习与研究 / 高校、留学与考试` | 大学生就业创业 | 2 | `学习课程与教育资源` | `small:student-career` | 全国大学生创业服务网项目与赛事平台首页；国家大学生就业服务平台个人设置页面 |
| 478 | `02_学习与研究 / 高校、留学与考试` | 学生优惠与教育资源 | 1 | `学习课程与教育资源` | `small:student-benefits` | 高校师生Apple微软GitHub等产品教育优惠购买指南 |
| 479 | `02_学习与研究 / 高校、留学与考试` | 安全生产职业培训 | 2 | `学习课程与教育资源` | `small:workplace-safety-training` | 中国安全生产协会新职业网络学院官方网站首页；企业安全管理岗位能力提升培训班举办通知 |
| 480 | `02_学习与研究 / 高校、留学与考试` | 广东教育考试 | 1 | `学习课程与教育资源` | `small:guangdong-education` | 广东省教育考试院官方网站与考试服务入口 |
| 481 | `02_学习与研究 / 高校、留学与考试` | 性教育参考 | 1 | `学习课程与教育资源` | `small:sexual-health` | 性爱姿势图文与动图科普参考网站 |
| 482 | `02_学习与研究 / 高校、留学与考试` | 机器学习课程 | 1 | `学习课程与教育资源` | `small:machine-learning-courses` | 斯坦福CS224W图机器学习课程资料与课程主页 |
| 483 | `02_学习与研究 / 高校、留学与考试` | 职业认证与培训 | 1 | `学习课程与教育资源` | `small:professional-certification` | CIW中国认证会员俱乐部加入与认证服务页面 |
| 484 | `02_学习与研究 / 高校、留学与考试` | 职业资格认证查询 | 2 | `学习课程与教育资源` | `small:professional-certification-services` | 职业技能等级评价机构公示信息官方查询系统；全国专业技术人员职业资格证书查询验证系统登录 |
| 485 | `02_学习与研究 / 高校、留学与考试` | 雅思备考与模拟考试 | 2 | `学习课程与教育资源` | `small:ielts-prep` | 雅思真题在线模拟测试与答案解析库；雅思免费机考模拟考试注册与练习系统 |
| 486 | `02_学习与研究 / 高校、留学与考试` | 高校官网 | 1 | `学习课程与教育资源` | `small:universities` | 四川大学官方网站综合门户首页 |
| 487 | `02_学习与研究 / 高校、留学与考试` | 产教融合动态 | 2 | `教育政策与职业教育` | `small:industry-education-integration` | 浙江传媒学院再次获批现代产业学院建设点新闻；山东文化产业职业学院加入数字创意AIGC产教融合共同体 |
| 488 | `02_学习与研究 / 高校、留学与考试` | 产教融合资料 | 1 | `教育政策与职业教育` | `small:industry-education-cooperation` | 南昌职业大学产学合作项目与校企合作手册 |
| 489 | `02_学习与研究 / 高校、留学与考试` | 山东教育政务 | 1 | `教育政策与职业教育` | `small:shandong-education-government` | 枣庄市教育局官方网站教育政务与通知公告 |
| 490 | `02_学习与研究 / 高校、留学与考试` | 工程教育合作 | 1 | `教育政策与职业教育` | `small:engineering-education-cooperation` | 天津大学ACNET工程技术教育认证合作项目网站 |
| 491 | `02_学习与研究 / 高校、留学与考试` | 教育与专业择校 | 1 | `教育政策与职业教育` | `small:education-guidance` | 2024年全国新能源科学与工程专业开设高校排名查询 |
| 492 | `02_学习与研究 / 高校、留学与考试` | 教育与大学 | 1 | `教育政策与职业教育` | `small:education-universities` | 博伊西州立大学官方网站与教育资源入口 |
| 493 | `02_学习与研究 / 高校、留学与考试` | 教育产业投资 | 1 | `教育政策与职业教育` | `small:education-investment` | 多鲸资本教育产业投资机构官方网站 |
| 494 | `02_学习与研究 / 高校、留学与考试` | 教育产业资讯 | 1 | `教育政策与职业教育` | `small:education-industry` | 芥末堆教育产业资讯与教育创新观察平台 |
| 495 | `02_学习与研究 / 高校、留学与考试` | 教育学术研究 | 1 | `教育政策与职业教育` | `small:education-research` | 中国社会科学杂志社教育学频道学术资讯页面 |
| 496 | `02_学习与研究 / 高校、留学与考试` | 教育政策与数字化 | 2 | `教育政策与职业教育` | `small:education-policy` | 怀进鹏谈国家教育数字化资源中心与服务平台建设进展；共建一带一路国家教育合作愿景与发展方向解读 |
| 497 | `02_学习与研究 / 高校、留学与考试` | 教育服务查询 | 1 | `教育政策与职业教育` | `small:education-services` | 强国助学学习情况与相关信息在线查询系统 |
| 498 | `02_学习与研究 / 高校、留学与考试` | 教育机构与院校 | 2 | `教育政策与职业教育` | `small:education-institutions` | 山东理工大学继续教育学院学院简介；淄博市技师学院信息工程系官方网站 |
| 499 | `02_学习与研究 / 高校、留学与考试` | 教育科研网络 | 1 | `教育政策与职业教育` | `small:education-networks` | 中国教育和科研计算机网CERNET官方网站首页 |
| 500 | `02_学习与研究 / 高校、留学与考试` | 教育认证机构 | 1 | `教育政策与职业教育` | `small:education-accreditation` | 悉尼协议研究院单位简介与机构信息 |
| 501 | `02_学习与研究 / 高校、留学与考试` | 出入境与证件 | 1 | `留学、签证与海外发展` | `small:immigration-travel-documents` | 国家移民管理局官方网站与出入境政务服务入口 |
| 502 | `02_学习与研究 / 高校、留学与考试` | 出国留学服务 | 1 | `留学、签证与海外发展` | `small:study-abroad-services` | 51offer英国澳洲美国出国留学申请服务平台 |
| 503 | `02_学习与研究 / 高校、留学与考试` | 奖学金申请 | 1 | `留学、签证与海外发展` | `small:scholarships` | 亚利桑那州立大学ScholarshipUniverse奖学金匹配系统 |
| 504 | `02_学习与研究 / 高校、留学与考试` | 学历认证与院校认可 | 2 | `留学、签证与海外发展` | `small:degree-recognition` | 德国anabin外国学历与院校认证信息查询门户；anabin国外高等教育机构认可状态数据库查询 |
| 505 | `02_学习与研究 / 高校、留学与考试` | 工作签证与海外就业 | 1 | `留学、签证与海外发展` | `small:work-visas` | 新加坡人力部就业准证Employment Pass申请说明 |
| 506 | `02_学习与研究 / 高校、留学与考试` | 日本旅行签证 | 2 | `留学、签证与海外发展` | `small:japan-travel-visa` | 日本驻华大使馆赴日旅游签证申请指南；日本国驻华大使馆赴日签证咨询官方指南 |
| 507 | `02_学习与研究 / 高校、留学与考试` | 日本签证与移民 | 1 | `留学、签证与海外发展` | `small:japan-immigration` | 日本护照免签国家与居留签证移民资料查询 |
| 508 | `02_学习与研究 / 高校、留学与考试` | 海外博士申请 | 1 | `留学、签证与海外发展` | `small:phd-study-abroad` | 特拉维夫大学中文官网博士研究生申请信息 |
| 509 | `02_学习与研究 / 高校、留学与考试` | 海外工作与移民 | 1 | `留学、签证与海外发展` | `small:immigration-work` | 奥地利红白红卡工作居留许可申请说明 |
| 510 | `02_学习与研究 / 高校、留学与考试` | 海外生活与签证 | 2 | `留学、签证与海外发展` | `small:travel-immigration` | 数字游民签证覆盖四十余国的申请攻略大全；润去新加坡留学就业移民信息服务平台主页 |
| 511 | `02_学习与研究 / 高校、留学与考试` | 美国税务与身份 | 1 | `留学、签证与海外发展` | `small:us-tax-immigration` | 美国国税局 IRS 个人报税识别号码 ITIN 申请指南 |
| 512 | `02_学习与研究 / 高校、留学与考试` | 香港优才与签证 | 2 | `留学、签证与海外发展` | `small:hong-kong-immigration` | 香港入境处优秀人才入境计划官方说明页面；香港政府优秀人才入境计划网上申请服务指南 |
| 513 | `02_学习与研究 / 高校、留学与考试` | 学历学籍与认证 | 2 | `考试、证书与职业资格` | `small:education-credentials` | 美国国家学生信息中心学历与学籍服务官网；美国国务院学术证书认证办理官方说明 |
| 514 | `02_学习与研究 / 高校、留学与考试` | 学历认证服务 | 2 | `考试、证书与职业资格` | `small:education-credentialing` | 中国留学服务中心学历学位认证在线服务平台；教育部留学服务中心国外学历认证院校查询入口 |
| 515 | `02_学习与研究 / 高校、留学与考试` | 教师资格认定 | 1 | `考试、证书与职业资格` | `small:teacher-certification` | 郴州市2023年第二批高中中职教师资格认定结果公告 |
| 516 | `02_学习与研究 / 高校、留学与考试` | 数字证书服务 | 1 | `考试、证书与职业资格` | `small:certificate-services` | 数字证书在线预受理系统管理后台入口 |
| 517 | `02_学习与研究 / 高校、留学与考试` | 考研公共课资料 | 1 | `考试、证书与职业资格` | `small:graduate-exam` | 2018年全国硕士研究生政治真题及答案解析 |
| 518 | `02_学习与研究 / 高校、留学与考试` | 考试题库与备考 | 2 | `考试、证书与职业资格` | `small:exam-preparation` | 粉笔题库考试练习题与学习资料下载平台；考试资料网试题题库与答案检索首页 |
| 519 | `02_学习与研究 / 高校、留学与考试` | 职业考试与刷题 | 2 | `考试、证书与职业资格` | `small:exam-prep` | F7职业技能鉴定考试题库与答案解析；物质与运动关系唯心主义错误试题解析 |
| 520 | `02_学习与研究 / 高校、留学与考试` | 职业能力证书 | 2 | `考试、证书与职业资格` | `small:professional-certificates` | 工信部教育与考试中心职业能力证书项目介绍；工业和信息化部人才交流中心证书服务页面 |
| 521 | `02_学习与研究 / 高校、留学与考试` | 计算机等级考试 | 1 | `考试、证书与职业资格` | `small:computer-exams` | 中国教育考试网全国计算机等级考试NCRE入口 |
| 522 | `02_学习与研究 / 高校、留学与考试` | 中学招生与升学信息 | 2 | `高校、院校与校园服务` | `small:secondary-school-admissions` | 九江一中2015年金榜题名学生名册；九江一中2018年招生简章与办学发展介绍 |
| 523 | `02_学习与研究 / 高校、留学与考试` | 中职招生查询 | 2 | `高校、院校与校园服务` | `small:secondary-vocational-admissions` | 湖南省中等职业教育阳光招生信息平台郴州学校查询；湖南省中等职业教育阳光招生平台学校信息详情 |
| 524 | `02_学习与研究 / 高校、留学与考试` | 大学与校园生活 | 1 | `高校、院校与校园服务` | `small:university-life` | 亚利桑那州立大学ASU官方服饰与纪念品商城 |
| 525 | `02_学习与研究 / 高校、留学与考试` | 大学与课程资源 | 1 | `高校、院校与校园服务` | `small:university-resources` | ClassReview亚利桑那州立大学课程评价与选课参考 |
| 526 | `02_学习与研究 / 高校、留学与考试` | 大学专业与课程查询 | 1 | `高校、院校与校园服务` | `small:university-study-search` | 明斯特大学中央学习咨询专业课程查询页面 |
| 527 | `02_学习与研究 / 高校、留学与考试` | 大学新闻与动态 | 1 | `高校、院校与校园服务` | `small:university-news` | 哥廷根大学新闻与校园动态发布页面 |
| 528 | `02_学习与研究 / 高校、留学与考试` | 大学课程资料 | 1 | `高校、院校与校园服务` | `small:university-courses` | 亚利桑那州立大学MEng组合项目报告说明录像 |
| 529 | `02_学习与研究 / 高校、留学与考试` | 山东技术学院 | 2 | `高校、院校与校园服务` | `small:shandong-technical-college` | 山东技术学院学院概况与办学情况介绍；山东技术学院招生信息网与报考资讯入口 |
| 530 | `02_学习与研究 / 高校、留学与考试` | 山东招生考试 | 2 | `高校、院校与校园服务` | `small:shandong-admissions-exams` | 山东省教育招生考试院官方网站考试招生服务入口；山东2019年初中后高职高师分市招生计划通知 |
| 531 | `02_学习与研究 / 高校、留学与考试` | 山东理工大学招生 | 1 | `高校、院校与校园服务` | `small:shandong-university-admissions` | 山东理工大学硕士研究生招生信息与公告 |
| 532 | `02_学习与研究 / 高校、留学与考试` | 山东理工大学资料 | 2 | `高校、院校与校园服务` | `small:shandong-university-info` | 山东理工大学工作简介与部门职责说明；山东理工大学计算机科学与技术学院官方网站 |
| 533 | `02_学习与研究 / 高校、留学与考试` | 山东职业院校招生 | 2 | `高校、院校与校园服务` | `small:shandong-vocational-admissions` | 山东文化产业职业学院招生信息官方网站；聊城职业技术学院2023年山东省招生计划设置 |
| 534 | `02_学习与研究 / 高校、留学与考试` | 山东高校招生 | 1 | `高校、院校与校园服务` | `small:shandong-college-admissions` | 枣庄学院2023年分专业招生计划查询 |
| 535 | `02_学习与研究 / 高校、留学与考试` | 教育与招生信息 | 1 | `高校、院校与校园服务` | `small:education-admissions` | 烟台工程职业技术学院普通高考招生信息页面 |
| 536 | `02_学习与研究 / 高校、留学与考试` | 校园生活社区 | 1 | `高校、院校与校园服务` | `small:campus-communities` | 大学生活质量指北高校体验与信息交流社区 |
| 537 | `02_学习与研究 / 高校、留学与考试` | 校园网络服务 | 1 | `高校、院校与校园服务` | `small:campus-it` | 同济大学校园VPN常见问题官方解答 |
| 538 | `02_学习与研究 / 高校、留学与考试` | 校园资源导航 | 1 | `高校、院校与校园服务` | `small:campus-navigation` | 北邮人BYR校园资源与网站导航入口 |
| 539 | `02_学习与研究 / 高校、留学与考试` | 湖北职业院校招生 | 2 | `高校、院校与校园服务` | `small:hubei-vocational-admissions` | 鄂州职业大学2023年招生报名入口与招生信息；2023年湖北省中职中专招生官方网站报名入口 |
| 540 | `02_学习与研究 / 高校、留学与考试` | 烟台职业教育政务 | 1 | `高校、院校与校园服务` | `small:yantai-vocational-education` | 烟台市政府门户网站职业教育专题信息页面 |
| 541 | `02_学习与研究 / 高校、留学与考试` | 职业学校名录 | 2 | `高校、院校与校园服务` | `small:vocational-school-directories` | 广州市增城区公办职业学校名单一览表；上饶市公布2023年中职学历教育招生资格学校名单通知 |
| 542 | `02_学习与研究 / 高校、留学与考试` | 职业教育行业资讯 | 2 | `高校、院校与校园服务` | `small:vocational-education-industry` | 现代职业教育网职业教育行业资讯平台；中国职业教育与成人教育行业资讯网站 |
| 543 | `02_学习与研究 / 高校、留学与考试` | 职业教育集团 | 1 | `高校、院校与校园服务` | `small:vocational-education-groups` | 尚职教育集团官方网站与职业教育业务信息 |
| 544 | `02_学习与研究 / 高校、留学与考试` | 高校发展规划 | 1 | `高校、院校与校园服务` | `small:college-development-plans` | 山东水利职业学院十四五事业发展规划全文 |
| 545 | `02_学习与研究 / 高校、留学与考试` | 高校学习平台 | 2 | `高校、院校与校园服务` | `small:university-learning` | 集美大学超星网络教学平台入口；Unipus高校外语教学与学习平台首页 |
| 546 | `02_学习与研究 / 高校、留学与考试` | 高校教育与实践 | 1 | `高校、院校与校园服务` | `small:university-education` | 厦门工学院工程坊实践教学平台页面 |
| 547 | `02_学习与研究 / 高校、留学与考试` | 高校资产经营资讯 | 1 | `高校、院校与校园服务` | `small:university-corporate-news` | 南昌大学资产经营有限责任公司新闻公告页面 |
| 548 | `03_创作与娱乐 / 内容创作与社交媒体` | 公众号文章导出工具 | 2 | `内容创作与社区资源` | `small:wechat-article-tools` | 微信公众号文章批量导出为Word和PDF的在线助手；微信公众号文章导出为PDF和Word的软件介绍页面 |
| 549 | `03_创作与娱乐 / 内容创作与社交媒体` | 哔哩哔哩下载工具 | 2 | `社交平台与内容运营` | `small:bilibili-download-tools` | 唧唧下载电脑客户端与哔哩哔哩视频下载；爱哔哩哔哩视频与音频 MP3 解析下载站 |
| 550 | `03_创作与娱乐 / 内容创作与社交媒体` | 社交与交友 | 1 | `社交平台与内容运营` | `small:social-dating` | Tinder在线约会交友与认识新朋友平台 |
| 551 | `03_创作与娱乐 / 内容创作与社交媒体` | 社交与聊天平台 | 1 | `社交平台与内容运营` | `small:social-platforms` | Juchats 无限爱主题在线聊天与社交平台 |
| 552 | `03_创作与娱乐 / 内容创作与社交媒体` | 社交关系分析 | 1 | `社交平台与内容运营` | `small:social-analysis` | Furland 推友关注关系图谱重定向页面 |
| 553 | `03_创作与娱乐 / 内容创作与社交媒体` | 社交媒体开发文档 | 1 | `社交平台与内容运营` | `small:social-media-development` | 小红书发布服务接口文档与调用说明 |
| 554 | `03_创作与娱乐 / 内容创作与社交媒体` | 社交媒体营销管理 | 1 | `社交平台与内容运营` | `small:social-media-marketing` | 云略一站式社交媒体营销管理系统 |
| 555 | `03_创作与娱乐 / 内容创作与社交媒体` | 社交媒体运营服务 | 1 | `社交平台与内容运营` | `small:social-media-services` | 社媒助手联系渠道与社交媒体运营服务咨询 |
| 556 | `03_创作与娱乐 / 内容创作与社交媒体` | 社保与退休金 | 2 | `社交平台与内容运营` | `small:china-social-security` | 中国城镇职工基本养老保险退休待遇在线测算工具；2024年五险一金社保缴费与到手工资计算器 |
| 557 | `03_创作与娱乐 / 内容创作与社交媒体` | 社保公积金与薪资 | 2 | `社交平台与内容运营` | `small:social-insurance-and-payroll` | 潮州市五险一金缴纳比例与社保基数查询；深圳市五险一金与税后工资在线计算器 |
| 558 | `03_创作与娱乐 / 内容创作与社交媒体` | AI 写作与内容创作 | 1 | `自媒体、博客与创作者工具` | `small:ai-writing-tools` | 创作王一站式人工智能内容创作平台登录页 |
| 559 | `03_创作与娱乐 / 内容创作与社交媒体` | 内容创作工具 | 2 | `自媒体、博客与创作者工具` | `small:content-creation` | 基于 MDX 的微信公众号图文排版在线编辑器；GeekCard：将文本生成精美分享卡片的在线工具 |
| 560 | `03_创作与娱乐 / 内容创作与社交媒体` | 内容创作工具 | 1 | `自媒体、博客与创作者工具` | `small:content-creation-tools` | MD2Card Markdown 转换知识卡片在线工具 |
| 561 | `03_创作与娱乐 / 内容创作与社交媒体` | 创作者会员与赞助 | 1 | `自媒体、博客与创作者工具` | `small:creator-membership` | 爱发电创作者会员订阅与粉丝赞助平台 |
| 562 | `03_创作与娱乐 / 内容创作与社交媒体` | 创作者平台与变现 | 2 | `自媒体、博客与创作者工具` | `small:creator-platforms` | 爱发电创作者平台新手入门与作品集使用指南；OnlyFans订阅制创作者内容平台首页 |
| 563 | `03_创作与娱乐 / 内容创作与社交媒体` | 博客建站 | 1 | `自媒体、博客与创作者工具` | `small:blogging-and-site-building` | 彭宏豪创建独立个人博客的建站记录文章 |
| 564 | `03_创作与娱乐 / 内容创作与社交媒体` | 建站与博客系统 | 2 | `自媒体、博客与创作者工具` | `small:cms-and-blogging` | Halo开源建站系统与个人博客部署平台；CodingMore网站文章内容后台管理系统 |
| 565 | `03_创作与娱乐 / 内容创作与社交媒体` | 自媒体运营 | 2 | `自媒体、博客与创作者工具` | `small:content-operations` | 易撰自媒体内容创作与选题辅助工具；鲁搬运营自媒体内容运营服务平台入口 |
| 566 | `03_创作与娱乐 / 游戏、动漫与休闲` | 云游戏服务 | 2 | `休闲娱乐与兴趣收藏` | `small:cloud-gaming` | 云玩加CloudPlayPlus云游戏平台官方网站；CloudPlayPlus在线云游戏平台网页应用入口 |
| 567 | `03_创作与娱乐 / 游戏、动漫与休闲` | 动漫创意产业机构 | 1 | `休闲娱乐与兴趣收藏` | `small:creative-industry-organizations` | 青岛市动漫创意产业协会工商企业信息查询 |
| 568 | `03_创作与娱乐 / 游戏、动漫与休闲` | 安卓游戏工具 | 1 | `休闲娱乐与兴趣收藏` | `small:android-gaming` | GameGuardian 安卓手游数值修改工具 APK 下载说明 |
| 569 | `03_创作与娱乐 / 游戏、动漫与休闲` | 小说与电子书检索 | 2 | `休闲娱乐与兴趣收藏` | `small:book-search` | BookLink.Me综合在线小说搜索引擎；Z-Library电子书与文献检索数字图书馆 |
| 570 | `03_创作与娱乐 / 游戏、动漫与休闲` | 影视娱乐 | 1 | `休闲娱乐与兴趣收藏` | `small:video-entertainment` | 小宝影院瑞克和莫蒂在线视频搜索结果 |
| 571 | `03_创作与娱乐 / 游戏、动漫与休闲` | 怀旧游戏 | 2 | `休闲娱乐与兴趣收藏` | `small:retro-gaming` | DOS.lol 浏览器在线运行经典 DOS 游戏与软件平台；老游戏在线玩经典怀旧游戏浏览器运行平台 |
| 572 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏展会活动 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-events` | 2021中国国际数码互动娱乐展览会购票信息 |
| 573 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏攻略与工具 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-guides-tools` | 云顶之弈S17阵容助手与专业阵容推荐工具 |
| 574 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏文化 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-culture` | MUD游戏玩家花色称谓与身份说明 |
| 575 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏服务 | 2 | `休闲娱乐与兴趣收藏` | `small:gaming-services` | 极狐游戏免费网络加速器官方网站；ROMS乐园免费游戏资源与ROM下载网站 |
| 576 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏硬件外设 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-hardware` | 盖世小鸡Gamesir游戏手柄官方产品网站 |
| 577 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏社区服务 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-communities` | ZERODREAM RACING FiveM 联机竞速服务器 |
| 578 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏网络加速 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-network-services` | 迅游网游加速器官方网络游戏加速服务 |
| 579 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏网络服务 | 2 | `休闲娱乐与兴趣收藏` | `small:gaming-network` | GI免费游戏网络加速器官方网站；游戏加速器品牌推荐与排行榜网站 |
| 580 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏自动化工具 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-automation` | Docker 自动领取 Epic 免费游戏镜像项目 |
| 581 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏行业研究 | 2 | `休闲娱乐与兴趣收藏` | `small:gaming-industry-research` | DataEye游戏行业数据报告与市场洞察中心；中国音数协游戏工委行业资讯与产业服务网站 |
| 582 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏视频创作 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-video-tools` | Capturelab游戏内容自动捕捉与短视频制作平台 |
| 583 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏账号与服务 | 2 | `休闲娱乐与兴趣收藏` | `small:gaming-accounts` | 英雄联盟北美服Riot游戏账号注册创建页面；Riot Games游戏账号资料与安全设置管理中心 |
| 584 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏资源 | 2 | `休闲娱乐与兴趣收藏` | `small:gaming-resources` | 主播同款游戏与相关资源聚合分享页面；王者图鉴：王者荣耀英雄与游戏资料查询站 |
| 585 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏资讯 | 1 | `休闲娱乐与兴趣收藏` | `small:gaming-news` | TouchArcade苹果iPhone与iPad游戏资讯网站 |
| 586 | `03_创作与娱乐 / 游戏、动漫与休闲` | 网络小说 | 1 | `休闲娱乐与兴趣收藏` | `small:online-fiction` | 白糖三两言情小说玉阶遥笼中燕阅读 |
| 587 | `03_创作与娱乐 / 游戏、动漫与休闲` | 网络小说阅读 | 1 | `休闲娱乐与兴趣收藏` | `small:web-fiction` | Wuxiaworld英文武侠与仙侠网络小说阅读平台 |
| 588 | `03_创作与娱乐 / 游戏、动漫与休闲` | 二次元视频社区 | 1 | `动漫、漫画与轻小说` | `small:anime-community` | AcFun弹幕视频与二次元内容社区首页 |
| 589 | `03_创作与娱乐 / 游戏、动漫与休闲` | 动漫与媒体管理 | 1 | `动漫、漫画与轻小说` | `small:anime-and-media` | AutoBangumi：自动追番下载与动画媒体库管理项目 |
| 590 | `03_创作与娱乐 / 游戏、动漫与休闲` | 动漫图片识别 | 2 | `动漫、漫画与轻小说` | `small:anime-image-search` | Ascii2d二次元动漫图片反向来源搜索工具；What Anime Is This动漫截图场景识别引擎 |
| 591 | `03_创作与娱乐 / 游戏、动漫与休闲` | 动漫漫画资源下载 | 1 | `动漫、漫画与轻小说` | `small:anime-manga-resources` | 进击的巨人动画漫画资源下载汇总 |
| 592 | `03_创作与娱乐 / 游戏、动漫与休闲` | 动漫识别工具 | 1 | `动漫、漫画与轻小说` | `small:anime-tools` | trace.moe以截图反向查询动漫出处的搜索工具 |
| 593 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏动漫社区 | 1 | `动漫、漫画与轻小说` | `small:gaming-anime` | Stage1 萨拉巴第一游戏动漫综合讨论论坛 |
| 594 | `03_创作与娱乐 / 游戏、动漫与休闲` | 漫画与娱乐 | 1 | `动漫、漫画与轻小说` | `small:comics-and-entertainment` | PicaWeb哔咔漫画网页版在线阅读入口 |
| 595 | `03_创作与娱乐 / 游戏、动漫与休闲` | 漫画资源服务 | 1 | `动漫、漫画与轻小说` | `small:manga-resources` | 集云数据看漫画资源聚合服务网站 |
| 596 | `03_创作与娱乐 / 游戏、动漫与休闲` | 电锯人漫画 | 2 | `动漫、漫画与轻小说` | `small:chainsaw-man-manga` | 电锯人第二部第九十八话汉化讨论页面；少年Jump+官方电锯人第二部第九十八话 |
| 597 | `03_创作与娱乐 / 游戏、动漫与休闲` | 网络漫画 | 1 | `动漫、漫画与轻小说` | `small:webcomics` | False Knees幽默讽刺四格漫画网站 |
| 598 | `03_创作与娱乐 / 游戏、动漫与休闲` | 视觉小说工具资源 | 1 | `动漫、漫画与轻小说` | `small:visual-novel-resources` | VNR吧共享目录与视觉小说工具资源索引 |
| 599 | `03_创作与娱乐 / 游戏、动漫与休闲` | 轻小说阅读 | 1 | `动漫、漫画与轻小说` | `small:light-novels` | 《OVERLORD 不死者之王》轻小说在线阅读 |
| 600 | `03_创作与娱乐 / 游戏、动漫与休闲` | 成人游戏资源 | 2 | `成人内容与敏感站点` | `small:adult-games` | xGames 成人向电子游戏资源平台首页；LewdGames离线成人向二维与三维游戏资源站 |
| 601 | `03_创作与娱乐 / 游戏、动漫与休闲` | AI模型社区 | 1 | `游戏、主机与模组资源` | `small:ai-model-communities` | 魔搭社区个人工作台与模型资源概览页面 |
| 602 | `03_创作与娱乐 / 游戏、动漫与休闲` | DNF自动化脚本 | 2 | `游戏、主机与模组资源` | `small:dnf-automation` | 神迹DNF全自动搬砖倍攻脚本下载页；蓬莱DNF全自动搬砖倍攻脚本下载页 |
| 603 | `03_创作与娱乐 / 游戏、动漫与休闲` | DNF辅助卡密销售 | 2 | `游戏、主机与模组资源` | `small:dnf-cheat-sales` | DNF飞机辅助充值卡密销售中心；179发卡网DNF低价提卡商品分类 |
| 604 | `03_创作与娱乐 / 游戏、动漫与休闲` | PC游戏修改器 | 2 | `游戏、主机与模组资源` | `small:pc-game-trainers` | FLiNG风灵月影PC游戏修改器与模组官网；布衣爱游戏的单机游戏修改器与升级档资源 |
| 605 | `03_创作与娱乐 / 游戏、动漫与休闲` | PC游戏论坛资源 | 1 | `游戏、主机与模组资源` | `small:pc-game-forums` | 游民星空论坛PC游戏策略分类资源列表 |
| 606 | `03_创作与娱乐 / 游戏、动漫与休闲` | PC游戏资讯 | 1 | `游戏、主机与模组资源` | `small:pc-game-news` | 游迅网最新单机游戏资讯新闻频道 |
| 607 | `03_创作与娱乐 / 游戏、动漫与休闲` | Switch主机论坛 | 1 | `游戏、主机与模组资源` | `small:switch-forums` | 91Wii论坛任天堂Nintendo Switch主机讨论区 |
| 608 | `03_创作与娱乐 / 游戏、动漫与休闲` | Switch使用教程 | 1 | `游戏、主机与模组资源` | `small:switch-guides` | Switch大气层使用Awoo安装NSP与XCI游戏教程 |
| 609 | `03_创作与娱乐 / 游戏、动漫与休闲` | Switch金手指资源 | 2 | `游戏、主机与模组资源` | `small:switch-cheats` | 91Wii论坛Switch金手指代码整合包下载帖；GBAtemp发布的SXOS与Atmosphere金手指代码合集 |
| 610 | `03_创作与娱乐 / 游戏、动漫与休闲` | 主机游戏资源 | 2 | `游戏、主机与模组资源` | `small:console-game-resources` | 游侠论坛PS2与PS1游戏全集资源下载帖；游侠NETSHOW论坛最全TV主机游戏资源版块 |
| 611 | `03_创作与娱乐 / 游戏、动漫与休闲` | 主机金手指论坛 | 1 | `游戏、主机与模组资源` | `small:console-cheat-forums` | 银河战士金手指主机玩家交流论坛首页 |
| 612 | `03_创作与娱乐 / 游戏、动漫与休闲` | 乙女游戏资源 | 2 | `游戏、主机与模组资源` | `small:otome-games` | 乙女游戏蝶之毒华之锁幻想夜话汉化版资源；蝶之毒华之锁汉化版全开存档游戏下载 |
| 613 | `03_创作与娱乐 / 游戏、动漫与休闲` | 内容审核与文本过滤 | 1 | `游戏、主机与模组资源` | `small:content-moderation` | 敏感词过滤检测开源工具使用说明文档 |
| 614 | `03_创作与娱乐 / 游戏、动漫与休闲` | 单机游戏下载 | 2 | `游戏、主机与模组资源` | `small:pc-game-downloads` | FFF 游戏站的49423单机游戏下载首页；FFF 游戏站的49423单机游戏 VIP 页面 |
| 615 | `03_创作与娱乐 / 游戏、动漫与休闲` | 在线游戏与娱乐 | 2 | `游戏、主机与模组资源` | `small:online-games` | 红色警戒二网页版全平台即时战略游戏；Chess.com 在线与电脑人工智能进行国际象棋对弈 |
| 616 | `03_创作与娱乐 / 游戏、动漫与休闲` | 在线生成器与小游戏 | 2 | `游戏、主机与模组资源` | `small:generators-and-games` | Maze Generator在线迷宫生成与打印谜题工具；Tikolu在线表情包混合生成与趣味图片制作工具 |
| 617 | `03_创作与娱乐 / 游戏、动漫与休闲` | 复古游戏与模拟器资源 | 1 | `游戏、主机与模组资源` | `small:retro-game-resources` | 老男人游戏网复古主机游戏与模拟器资源 |
| 618 | `03_创作与娱乐 / 游戏、动漫与休闲` | 大模型评测与排行 | 1 | `游戏、主机与模组资源` | `small:ai-model-evaluation` | LMArena全球人工智能大模型能力排行榜 |
| 619 | `03_创作与娱乐 / 游戏、动漫与休闲` | 大模型评测与排行 | 1 | `游戏、主机与模组资源` | `small:ai-model-reviews` | 林哥大模型野榜主流人工智能模型评测排行 |
| 620 | `03_创作与娱乐 / 游戏、动漫与休闲` | 大语言模型 | 2 | `游戏、主机与模组资源` | `small:llm-models` | Meta Llama 3 开源大语言模型官方产品介绍页面；OpenRouter上的Llama 3 Soliloquy八十亿参数模型 |
| 621 | `03_创作与娱乐 / 游戏、动漫与休闲` | 安卓模拟器 | 1 | `游戏、主机与模组资源` | `small:android-emulators` | 夜神安卓模拟器电脑版与手游运行平台下载 |
| 622 | `03_创作与娱乐 / 游戏、动漫与休闲` | 手机刷机与改装 | 1 | `游戏、主机与模组资源` | `small:mobile-device-modding` | 华为 Mate 30 Pro 安装操作系统的分步教程 |
| 623 | `03_创作与娱乐 / 游戏、动漫与休闲` | 旅行住宿预订 | 1 | `游戏、主机与模组资源` | `small:travel-accommodation` | Booking.com缤客酒店民宿与公寓预订平台 |
| 624 | `03_创作与娱乐 / 游戏、动漫与休闲` | 时尚与模特 | 1 | `游戏、主机与模组资源` | `small:fashion-and-models` | MOKO美空模特与时尚娱乐人才展示平台 |
| 625 | `03_创作与娱乐 / 游戏、动漫与休闲` | 模拟器使用教程 | 2 | `游戏、主机与模组资源` | `small:emulator-guides` | RPCS3运行战神3的设置参数与演示视频；电玩巴士GBA模拟器下载与使用教程 |
| 626 | `03_创作与娱乐 / 游戏、动漫与休闲` | 模拟器金手指资源 | 1 | `游戏、主机与模组资源` | `small:emulator-cheats` | EmuSkr多平台模拟器游戏金手指代码资源 |
| 627 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏下载资源 | 2 | `游戏、主机与模组资源` | `small:game-download-resources` | 蓝奏云EarlyAccess早期访问资源分享目录；GBT乐赏游戏空间大型免费游戏资源目录 |
| 628 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏与模组工具 | 1 | `游戏、主机与模组资源` | `small:games-and-mods` | 太吾绘卷脚本训练器资源文件下载目录 |
| 629 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏价格与数据查询 | 1 | `游戏、主机与模组资源` | `small:game-price-tracking` | SteamDB 游戏价格历史与Steam数据库查询 |
| 630 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏价格追踪 | 1 | `游戏、主机与模组资源` | `small:game-prices` | Lossless Scaling在Steam平台的价格历史 |
| 631 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏修改讨论 | 1 | `游戏、主机与模组资源` | `small:game-cheat-discussions` | Dead In Bermuda反作弊检测与修复讨论 |
| 632 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏修改资源 | 1 | `游戏、主机与模组资源` | `small:game-cheat-resources` | 作弊代码网游戏资源分类下载页面 |
| 633 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏商店账户 | 1 | `游戏、主机与模组资源` | `small:game-store-accounts` | Green Man Gaming 游戏商店个人账户资料 |
| 634 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏工具与社区 | 2 | `游戏、主机与模组资源` | `small:game-tools-and-communities` | 风灵月影游戏修改器下载与用户交流论坛；Steam游戏账号分享与离线联机资源论坛 |
| 635 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏推荐与资讯 | 1 | `游戏、主机与模组资源` | `small:game-discovery` | 游研社玩什么游戏推荐与内容索引 |
| 636 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏文件工具 | 1 | `游戏、主机与模组资源` | `small:game-development-tools` | 从英雄联盟游戏文件中提取音频的工具项目 |
| 637 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏比价工具 | 1 | `游戏、主机与模组资源` | `small:game-price-tools` | IsThereAnyDeal 应用授权与账户连接页面 |
| 638 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏维基资料 | 1 | `游戏、主机与模组资源` | `small:game-wikis` | 脑叶公司中文维基百科与游戏资料库 |
| 639 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏美术与设计 | 1 | `游戏、主机与模组资源` | `small:game-art-and-design` | TatoomCity韩国游戏设计师Yi Lee个人主页 |
| 640 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏翻译工具 | 1 | `游戏、主机与模组资源` | `small:game-translation` | GTA5游戏聊天专用在线翻译工具 |
| 641 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏虚拟物品交易 | 1 | `游戏、主机与模组资源` | `small:game-item-trading` | DD373游戏账号装备与点卡交易个人中心 |
| 642 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏账号管理 | 2 | `游戏、主机与模组资源` | `small:game-accounts` | Paradox Interactive 账号管理控制台；拳头游戏Riot账号资料与安全管理页面 |
| 643 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏资料攻略 | 1 | `游戏、主机与模组资源` | `small:game-guides` | 泰拉瑞亚世界吞噬怪官方中文百科词条 |
| 644 | `03_创作与娱乐 / 游戏、动漫与休闲` | 游戏资源下载 | 1 | `游戏、主机与模组资源` | `small:game-downloads` | 蒸汽游戏宝库Steam单机游戏下载资源站 |
| 645 | `03_创作与娱乐 / 游戏、动漫与休闲` | 独立游戏 | 1 | `游戏、主机与模组资源` | `small:indie-games` | ANDYLAW.GAMES 独立游戏作品集合 |
| 646 | `03_创作与娱乐 / 游戏、动漫与休闲` | 独立游戏资源 | 1 | `游戏、主机与模组资源` | `small:indie-game-resources` | Indie Expo今日热门独立网页游戏下载榜单 |
| 647 | `03_创作与娱乐 / 游戏、动漫与休闲` | 电子游戏资源 | 2 | `游戏、主机与模组资源` | `small:game-resources` | 668游戏仓库多平台电子游戏资源中心；Switch520任天堂Switch游戏资源网站 |
| 648 | `03_创作与娱乐 / 游戏、动漫与休闲` | 科创教育平台 | 1 | `游戏、主机与模组资源` | `small:steam-education-platforms` | 枣庄STEAM科创教育云平台课程资源入口 |
| 649 | `03_创作与娱乐 / 游戏、动漫与休闲` | 粉丝游戏项目 | 1 | `游戏、主机与模组资源` | `small:fan-game-projects` | BIOHAZARD 初代粉丝重制游戏项目页面 |
| 650 | `03_创作与娱乐 / 游戏、动漫与休闲` | 红色警戒模组资料 | 2 | `游戏、主机与模组资源` | `small:red-alert-mods` | 红色警戒2心灵终结盟军单位介绍第七篇；心灵终结3.3在Windows 10兼容运行设置教程 |
| 651 | `03_创作与娱乐 / 游戏、动漫与休闲` | 网页游戏 | 1 | `游戏、主机与模组资源` | `small:web-games` | 拼命玩游戏平台的死亡独轮车 Happy Wheels 页面 |
| 652 | `03_创作与娱乐 / 游戏、动漫与休闲` | 音乐节奏网页游戏 | 2 | `游戏、主机与模组资源` | `small:rhythm-web-games` | Taiko Web网页版太鼓达人节奏游戏；Melody Muncher Deluxe音乐节奏小游戏 |
| 653 | `03_创作与娱乐 / 游戏、动漫与休闲` | 鬼谷八荒模组 | 1 | `游戏、主机与模组资源` | `small:guigubahuang-mods` | 鬼谷八荒功法一格与内功九格模组制作教程 |
| 654 | `03_创作与娱乐 / 视频、音频与影视` | AI录音与笔记 | 2 | `媒体平台与观看服务` | `small:ai-note-tools` | Plaud智能录音设备与语音笔记应用入口；Plaud网页端社区模板与语音笔记资源 |
| 655 | `03_创作与娱乐 / 视频、音频与影视` | 动画影视资源 | 1 | `媒体平台与观看服务` | `small:animation-resources` | 英雄联盟双城之战第二季网盘资源下载页面 |
| 656 | `03_创作与娱乐 / 视频、音频与影视` | 媒体视频平台 | 1 | `媒体平台与观看服务` | `small:media-platforms` | 浙江卫视Z视介平台品牌宣传视频页面 |
| 657 | `03_创作与娱乐 / 视频、音频与影视` | 影视媒体库管理 | 1 | `媒体平台与观看服务` | `small:media-library` | tinyMediaManager影视媒体库刮削器下载页 |
| 658 | `03_创作与娱乐 / 视频、音频与影视` | 影视数据与推荐 | 2 | `媒体平台与观看服务` | `small:media-data` | MovieLens电影评分数据集与推荐系统研究平台；South Plus论坛讨论Movie Data Capture影视刮削替代方案 |
| 659 | `03_创作与娱乐 / 视频、音频与影视` | 影视资源论坛 | 1 | `媒体平台与观看服务` | `small:media-forums` | 耐卡影音论坛欧美日韩影视资源交流区 |
| 660 | `03_创作与娱乐 / 视频、音频与影视` | 剧集下载资源 | 1 | `影视、剧集与字幕资源` | `small:tv-downloads` | 人人影视剧集下载分享站点 |
| 661 | `03_创作与娱乐 / 视频、音频与影视` | 国语影视播放 | 1 | `影视、剧集与字幕资源` | `small:streaming-chinese` | 9亿好国语配音电影与韩剧在线观看平台 |
| 662 | `03_创作与娱乐 / 视频、音频与影视` | 在线影视播放 | 1 | `影视、剧集与字幕资源` | `small:streaming-movies` | 野荷塘在线影视与伦理影片播放网站 |
| 663 | `03_创作与娱乐 / 视频、音频与影视` | 字幕组资源 | 2 | `影视、剧集与字幕资源` | `small:subtitle-groups` | 电波字幕组美英喜剧字幕与资源主页；SubHD远鉴字幕组主页与作品集合 |
| 664 | `03_创作与娱乐 / 视频、音频与影视` | 安卓电视应用 | 1 | `影视、剧集与字幕资源` | `small:android-tv-apps` | Emotn Store安卓电视盒子应用商店下载 |
| 665 | `03_创作与娱乐 / 视频、音频与影视` | 影视内容 | 1 | `影视、剧集与字幕资源` | `small:film-and-tv` | 邪教档案之末日风暴影视直播回放页面 |
| 666 | `03_创作与娱乐 / 视频、音频与影视` | 影视搜索工具 | 1 | `影视、剧集与字幕资源` | `small:movie-search` | 茶杯狐电影影视资源搜索与发现平台 |
| 667 | `03_创作与娱乐 / 视频、音频与影视` | 影视资源导航 | 2 | `影视、剧集与字幕资源` | `small:film-tv-navigation` | 549影視森林電影與追劇資源導航站；SuSu分享影音與網路資源聚合網站 |
| 668 | `03_创作与娱乐 / 视频、音频与影视` | 影视资源搜索 | 1 | `影视、剧集与字幕资源` | `small:film-tv-search` | 疯狂影视聚合电影电视剧资源搜索引擎首页 |
| 669 | `03_创作与娱乐 / 视频、音频与影视` | 流媒体解锁服务 | 1 | `影视、剧集与字幕资源` | `small:streaming-unlock` | 老姨子 Netflix 解锁网络服务页面 |
| 670 | `03_创作与娱乐 / 视频、音频与影视` | 流媒体订阅服务 | 2 | `影视、剧集与字幕资源` | `small:streaming-services` | 奈飞Pro流媒体高清套餐购买服务页面；奈飞小铺流媒体影音账号合租服务平台 |
| 671 | `03_创作与娱乐 / 视频、音频与影视` | 漫威剧集 | 1 | `影视、剧集与字幕资源` | `small:marvel-series` | 旺达幻视第六集在线之家播放页面 |
| 672 | `03_创作与娱乐 / 视频、音频与影视` | 电影下载资源 | 2 | `影视、剧集与字幕资源` | `small:movie-downloads` | 武帝电影天堂最新电影与迅雷下载资源；飘花电影网高清电影迅雷下载资源站 |
| 673 | `03_创作与娱乐 / 视频、音频与影视` | 电影在线观看资源 | 1 | `影视、剧集与字幕资源` | `small:movie-streaming` | 动作电影影片列表与在线观看页面 |
| 674 | `03_创作与娱乐 / 视频、音频与影视` | 电影推荐 | 1 | `影视、剧集与字幕资源` | `small:movie-recommendations` | 努努影院高评分科幻电影推荐列表 |
| 675 | `03_创作与娱乐 / 视频、音频与影视` | 电影画面与影视参考 | 1 | `影视、剧集与字幕资源` | `small:film-reference` | FILMGRAB按导演字母索引浏览电影剧照 |
| 676 | `03_创作与娱乐 / 视频、音频与影视` | 电影票房与行业数据 | 2 | `影视、剧集与字幕资源` | `small:film-industry-data` | 猫眼专业版电影实时票房数据看板；猫眼专业版电影票房市场分析数据平台 |
| 677 | `03_创作与娱乐 / 视频、音频与影视` | 直播制作工具 | 2 | `影视、剧集与字幕资源` | `small:live-streaming-tools` | OBS插件中心与直播制作插件产品官网；READMEobs直播画面美颜插件下载介绍 |
| 678 | `03_创作与娱乐 / 视频、音频与影视` | 直播平台管理 | 1 | `影视、剧集与字幕资源` | `small:live-streaming` | 斗鱼直播个人中心已关注主播列表 |
| 679 | `03_创作与娱乐 / 视频、音频与影视` | 直播行业研究 | 1 | `影视、剧集与字幕资源` | `small:live-streaming-industry` | 直播观察行业数据与直播生态研究平台 |
| 680 | `03_创作与娱乐 / 视频、音频与影视` | 美剧在线观看 | 2 | `影视、剧集与字幕资源` | `small:streaming-american-series` | 硅谷第一季美剧全集在线观看页面；谎言屋第一季欧美剧全集在线观看 |
| 681 | `03_创作与娱乐 / 视频、音频与影视` | AI 视频创作 | 2 | `视频制作、播放器与媒体工具` | `small:ai-video-creation` | SkyReels 人工智能故事可视化视频生成平台；LibTV面向专业创作者的人工智能视频制作工具 |
| 682 | `03_创作与娱乐 / 视频、音频与影视` | AI 设计与视频生成 | 2 | `视频制作、播放器与媒体工具` | `small:ai-design-video` | SVGAnimate AI 生成 SVG 动画与 HTML 视频工具；想得美：基于人工智能的视频生成工具 |
| 683 | `03_创作与娱乐 / 视频、音频与影视` | 创意视频与动态视觉 | 1 | `视频制作、播放器与媒体工具` | `small:video-inspiration` | 开眼Eyepetizer精选短视频发现与观看平台 |
| 684 | `03_创作与娱乐 / 视频、音频与影视` | 在线影视资源 | 1 | `视频制作、播放器与媒体工具` | `small:online-video` | 电影先生在线影视资源与影片播放网站 |
| 685 | `03_创作与娱乐 / 视频、音频与影视` | 媒体中心与家庭影音 | 2 | `视频制作、播放器与媒体工具` | `small:media-centers` | Kodi 开源家庭影院媒体中心 Windows 版本下载页面；Kodi 无法连接官方插件库错误的排查解决教程 |
| 686 | `03_创作与娱乐 / 视频、音频与影视` | 媒体播放器 | 2 | `视频制作、播放器与媒体工具` | `small:media-players` | PotPlayer多媒体视频播放器中文语言官网入口；PotPlayer全能影音播放器官方网站与下载入口 |
| 687 | `03_创作与娱乐 / 视频、音频与影视` | 影视播放应用 | 1 | `视频制作、播放器与媒体工具` | `small:video-apps` | 大师兄影视手机全平台视频应用官网 |
| 688 | `03_创作与娱乐 / 视频、音频与影视` | 成人影片资源 | 1 | `视频制作、播放器与媒体工具` | `small:adult-video-resources` | OneJAV成人影片种子资源索引网站 |
| 689 | `03_创作与娱乐 / 视频、音频与影视` | 成人视频下载工具 | 1 | `视频制作、播放器与媒体工具` | `small:adult-video-tools` | XXXSave在线视频下载工具中文首页 |
| 690 | `03_创作与娱乐 / 视频、音频与影视` | 成人视频工具社区 | 1 | `视频制作、播放器与媒体工具` | `small:adult-video-community` | JavPlayer中文交流论坛与使用讨论专区 |
| 691 | `03_创作与娱乐 / 视频、音频与影视` | 成人视频网站 | 1 | `视频制作、播放器与媒体工具` | `small:adult-video-sites` | MissAV视频网站与成人内容浏览入口 |
| 692 | `03_创作与娱乐 / 视频、音频与影视` | 摄影与视频服务 | 1 | `视频制作、播放器与媒体工具` | `small:photography-video` | Whitefish蒙大拿州婚礼与活动视频摄影服务 |
| 693 | `03_创作与娱乐 / 视频、音频与影视` | 视频与图像处理 | 2 | `视频制作、播放器与媒体工具` | `small:video-image-tools` | Unscreen在线自动移除视频背景工具；傲软免费在线视频转换GIF动图制作工具 |
| 694 | `03_创作与娱乐 / 视频、音频与影视` | 视频与媒体服务 | 2 | `视频制作、播放器与媒体工具` | `small:video-and-media` | movie-web在线视频影视内容聚合播放应用；在线上传字幕文件并自动翻译的字幕处理工具 |
| 695 | `03_创作与娱乐 / 视频、音频与影视` | 视频制作素材 | 1 | `视频制作、播放器与媒体工具` | `small:video-production` | 影视飓风视频制作素材资源页面 |
| 696 | `03_创作与娱乐 / 视频、音频与影视` | 视频剪辑工具 | 2 | `视频制作、播放器与媒体工具` | `small:video-editing` | AutoCut面向竖屏视频的自动剪辑在线工具；Auto-Editor自动剪辑软件的安装使用文档页面 |
| 697 | `03_创作与娱乐 / 视频、音频与影视` | 视频营销工具 | 2 | `视频制作、播放器与媒体工具` | `small:video-marketing` | 极睿科技iCut智能视频剪辑产品功能介绍页面；极睿科技引流宝短视频营销获客产品官方网站 |
| 698 | `03_创作与娱乐 / 视频、音频与影视` | AI 音乐 | 1 | `音乐、音频与播客` | `small:ai-music` | Suno AI 生成歌曲夜曲的播放与分享页面 |
| 699 | `03_创作与娱乐 / 视频、音频与影视` | 专业音响设备 | 1 | `音乐、音频与播客` | `small:professional-audio` | 东莞三基音响专业音箱功放产品列表 |
| 700 | `03_创作与娱乐 / 视频、音频与影视` | 无损音乐资源社区 | 1 | `音乐、音频与播客` | `small:lossless-music-resources` | HiFiNi音乐磁场无损音乐资源社区 |
| 701 | `03_创作与娱乐 / 视频、音频与影视` | 订阅与流媒体 | 1 | `音乐、音频与播客` | `small:subscriptions-and-media` | 通过 Google Play 开通 Spotify Premium 订阅教程 |
| 702 | `03_创作与娱乐 / 视频、音频与影视` | 音乐 | 1 | `音乐、音频与播客` | `small:music` | 安室奈美惠与David Guetta歌曲在线播放 |
| 703 | `03_创作与娱乐 / 视频、音频与影视` | 音乐下载工具 | 1 | `音乐、音频与播客` | `small:music-download-tools` | MusicTools全网无损音乐下载软件官网 |
| 704 | `03_创作与娱乐 / 视频、音频与影视` | 音乐下载资源 | 2 | `音乐、音频与播客` | `small:music-downloads` | 宝贝DJ音乐网高品质DJ舞曲试听下载；KHInsider电子游戏原声音乐与OST免费下载站 |
| 705 | `03_创作与娱乐 / 视频、音频与影视` | 音乐与媒体工具 | 1 | `音乐、音频与播客` | `small:music-and-media` | BetterNCM 网易云音乐客户端插件与扩展资源页面 |
| 706 | `03_创作与娱乐 / 视频、音频与影视` | 音乐与音频 | 2 | `音乐、音频与播客` | `small:music-and-audio` | 喜马拉雅听书客户端官方下载与安装入口页面；Spotify歌手Minah单曲作品列表与音乐播放页 |
| 707 | `03_创作与娱乐 / 视频、音频与影视` | 音乐创作与配乐 | 2 | `音乐、音频与播客` | `small:music-creation` | Epidemic Sound商用免版税音乐与音效曲库；Jukedeck人工智能生成免版税视频配乐工具 |
| 708 | `03_创作与娱乐 / 视频、音频与影视` | 音乐学习 | 2 | `音乐、音频与播客` | `small:music-learning` | Flowkey在线钢琴课程与曲谱练习平台；Ableton Learning Music免费互动音乐制作课程 |
| 709 | `03_创作与娱乐 / 视频、音频与影视` | 音乐搜索与下载 | 1 | `音乐、音频与播客` | `small:music-search` | Slider音乐搜索与免费音频下载网站 |
| 710 | `03_创作与娱乐 / 视频、音频与影视` | 音乐播放与发现 | 1 | `音乐、音频与播客` | `small:music-listening` | Listen 1跨平台歌曲搜索与在线音乐播放器 |
| 711 | `03_创作与娱乐 / 视频、音频与影视` | 音乐文化与流派 | 1 | `音乐、音频与播客` | `small:music-culture` | Ishkur电子音乐流派发展与知识科普指南 |
| 712 | `03_创作与娱乐 / 视频、音频与影视` | 音乐电台与声音探索 | 2 | `音乐、音频与播客` | `small:music-radio` | Radio Garden全球广播电台地图实时收听服务；Radio Aporee全球地图环境声音采集档案 |
| 713 | `03_创作与娱乐 / 视频、音频与影视` | 音乐节与演唱会 | 1 | `音乐、音频与播客` | `small:music-events` | 2023 年音乐节与演唱会时间阵容购票汇总 |
| 714 | `03_创作与娱乐 / 视频、音频与影视` | 音乐订阅与合租 | 2 | `音乐、音频与播客` | `small:music-subscriptions` | 悦音坊 Spotify 正版音乐会员合租服务平台；Spotify 合租平台价格、地区与发车渠道对比指南 |
| 715 | `03_创作与娱乐 / 视频、音频与影视` | 音乐资源 | 2 | `音乐、音频与播客` | `small:music-resources` | MP3BST 免费音乐文件检索目录网站；熊猫无损音乐下载与歌词简谱资源网站 |
| 716 | `03_创作与娱乐 / 视频、音频与影视` | 音乐辅助工具 | 1 | `音乐、音频与播客` | `small:music-utilities` | 网易云音乐歌曲封面图片在线下载助手 |
| 717 | `03_创作与娱乐 / 视频、音频与影视` | 音效与音频素材 | 1 | `音乐、音频与播客` | `small:audio-assets` | Sonniss GameAudioGDC免费游戏音效资源包 |
| 718 | `03_创作与娱乐 / 视频、音频与影视` | 音频与语音服务 | 2 | `音乐、音频与播客` | `small:audio-services` | Babel Audio 音频处理与语音服务平台；Babel Audio 音频项目控制台登录入口 |
| 719 | `03_创作与娱乐 / 视频、音频与影视` | 音频格式转换工具 | 1 | `音乐、音频与播客` | `small:audio-conversion-tools` | 网易云音乐NCM格式在线转换为MP3工具 |
| 720 | `03_创作与娱乐 / 视频、音频与影视` | 音频编辑工具 | 1 | `音乐、音频与播客` | `small:audio-editing` | Audacity 开源音频录制编辑与处理软件下载 |
| 721 | `03_创作与娱乐 / 设计、图片与视觉素材` | 3D 模型与素材 | 1 | `3D 模型、渲染与数字人` | `small:3d-assets` | 飞凡 F7 Rising Auto 2023 汽车三维模型资源 |
| 722 | `03_创作与娱乐 / 设计、图片与视觉素材` | 3D角色与数字人 | 1 | `3D 模型、渲染与数字人` | `small:3d-character-tools` | Unreal Engine MetaHuman高精度数字人创建平台 |
| 723 | `03_创作与娱乐 / 设计、图片与视觉素材` | 三维艺术作品 | 1 | `3D 模型、渲染与数字人` | `small:3d-art-inspiration` | The French Monkey超级C4D每日创作项目作品集 |
| 724 | `03_创作与娱乐 / 设计、图片与视觉素材` | 三维设计工具 | 1 | `3D 模型、渲染与数字人` | `small:3d-design-tools` | Superhuman 在线三维角色创建与人物建模工具 |
| 725 | `03_创作与娱乐 / 设计、图片与视觉素材` | 三维设计教程 | 1 | `3D 模型、渲染与数字人` | `small:3d-design-tutorials` | C4D灯管字发光文字效果制作课程 |
| 726 | `03_创作与娱乐 / 设计、图片与视觉素材` | 人工智能三维工具 | 1 | `3D 模型、渲染与数字人` | `small:ai-3d-tools` | Hyper3D Rodin人工智能三维模型生成用户案例集 |
| 727 | `03_创作与娱乐 / 设计、图片与视觉素材` | Adobe 软件资源 | 2 | `Adobe、Photoshop 与创作软件` | `small:adobe-resources` | Adobe 软件资源下载与安装信息汇总页面；Adobe Creative Cloud 软件版本与补丁资源页面 |
| 728 | `03_创作与娱乐 / 设计、图片与视觉素材` | Photoshop特效素材 | 1 | `Adobe、Photoshop 与创作软件` | `small:photoshop-effects` | GraphicRiver三维碎片消散效果Photoshop动作素材 |
| 729 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计插件 | 1 | `Adobe、Photoshop 与创作软件` | `small:design-plugins` | PS拉框助手用于Photoshop图表快速制作插件 |
| 730 | `03_创作与娱乐 / 设计、图片与视觉素材` | AI图像素材 | 1 | `图片、摄影与视觉资源` | `small:ai-image-assets` | Generated Photos人工生成肖像与人物照片素材库 |
| 731 | `03_创作与娱乐 / 设计、图片与视觉素材` | 前端可视化开发 | 1 | `图片、摄影与视觉资源` | `small:frontend-visualization-libraries` | Apache ECharts开源数据可视化图表库官网 |
| 732 | `03_创作与娱乐 / 设计、图片与视觉素材` | 前端数据可视化 | 1 | `图片、摄影与视觉资源` | `small:frontend-visualization` | AntV蚂蚁集团数据可视化产品与图表库 |
| 733 | `03_创作与娱乐 / 设计、图片与视觉素材` | 动物图片素材 | 1 | `图片、摄影与视觉资源` | `small:animal-images` | Cutest Paw全球可爱宠物与动物摄影图片站 |
| 734 | `03_创作与娱乐 / 设计、图片与视觉素材` | 历史复古图片 | 1 | `图片、摄影与视觉资源` | `small:historical-photos` | New Old Stock旧时代生活影像与黑白历史照片库 |
| 735 | `03_创作与娱乐 / 设计、图片与视觉素材` | 可视化与图表工具 | 1 | `图片、摄影与视觉资源` | `small:visualization-tools` | Knight Lab 可视化交互时间轴在线制作工具 |
| 736 | `03_创作与娱乐 / 设计、图片与视觉素材` | 可视化协作工具 | 2 | `图片、摄影与视觉资源` | `small:visual-collaboration-tools` | Bullmind在线流程图绘制与模板推荐工作台；Felt在线地图制作协作与地理数据可视化平台 |
| 737 | `03_创作与娱乐 / 设计、图片与视觉素材` | 图像处理与分析 | 1 | `图片、摄影与视觉资源` | `small:image-processing` | ImageJ 开源科研图像处理与分析软件平台 |
| 738 | `03_创作与娱乐 / 设计、图片与视觉素材` | 图像设计工具 | 1 | `图片、摄影与视觉资源` | `small:image-design-tools` | Fermat Toolmaker在线贴纸生成器工具 |
| 739 | `03_创作与娱乐 / 设计、图片与视觉素材` | 图片下载工具 | 1 | `图片、摄影与视觉资源` | `small:image-download-tools` | PIXID在线浏览与下载Pixiv图片的简易工具 |
| 740 | `03_创作与娱乐 / 设计、图片与视觉素材` | 图片与设计素材 | 2 | `图片、摄影与视觉资源` | `small:image-assets` | Everypixel多图库商业图片聚合搜索引擎；CartoonStock 商业卡通插画与漫画素材图库 |
| 741 | `03_创作与娱乐 / 设计、图片与视觉素材` | 图片拼图与合成 | 2 | `图片、摄影与视觉资源` | `small:image-composition` | Icons8 Moose在线场景拼图与人物图片合成工具；美图秀秀网页版模板拼图与海报图片制作工具 |
| 742 | `03_创作与娱乐 / 设计、图片与视觉素材` | 图片生成工具 | 1 | `图片、摄影与视觉资源` | `small:image-generators` | 在线装逼图片生成与趣味制图工具网站 |
| 743 | `03_创作与娱乐 / 设计、图片与视觉素材` | 壁纸与图片资源 | 1 | `图片、摄影与视觉资源` | `small:wallpapers-images` | Wallpaper Cave高清电脑与手机壁纸图库 |
| 744 | `03_创作与娱乐 / 设计、图片与视觉素材` | 复古与公共领域图像 | 1 | `图片、摄影与视觉资源` | `small:vintage-and-archive-images` | Viintage公共领域复古图片免费下载资源库 |
| 745 | `03_创作与娱乐 / 设计、图片与视觉素材` | 封面图片设计工具 | 1 | `图片、摄影与视觉资源` | `small:cover-image-tools` | PicProse 在线文章封面图片生成工具 |
| 746 | `03_创作与娱乐 / 设计、图片与视觉素材` | 建筑可视化 | 1 | `图片、摄影与视觉资源` | `small:architectural-visualization` | VizPeople建筑可视化三维模型与行业资讯博客 |
| 747 | `03_创作与娱乐 / 设计、图片与视觉素材` | 建筑摄影图片 | 1 | `图片、摄影与视觉资源` | `small:architecture-photography` | 集美大学新图书馆建筑的正版库存照片详情 |
| 748 | `03_创作与娱乐 / 设计、图片与视觉素材` | 成人图像与同人内容 | 1 | `图片、摄影与视觉资源` | `small:adult-images` | Rule34成人视频与同人图像标签站点 |
| 749 | `03_创作与娱乐 / 设计、图片与视觉素材` | 摄影与图像处理 | 1 | `图片、摄影与视觉资源` | `small:photo-editing` | darktable 开源 RAW 照片处理与摄影工作流软件 |
| 750 | `03_创作与娱乐 / 设计、图片与视觉素材` | 摄影与视觉灵感 | 2 | `图片、摄影与视觉资源` | `small:photo-inspiration` | 法国PHOTO摄影杂志官方网站与封面作品；Flickr热门标签浏览页：按主题发现摄影图片与作品 |
| 751 | `03_创作与娱乐 / 设计、图片与视觉素材` | 摄影作品社区 | 1 | `图片、摄影与视觉资源` | `small:photography-community` | 500px全球摄影师社区与高质量摄影作品平台 |
| 752 | `03_创作与娱乐 / 设计、图片与视觉素材` | 摄影学习与教程 | 1 | `图片、摄影与视觉资源` | `small:photography-learning` | Digital Photography School摄影技巧与教程博客 |
| 753 | `03_创作与娱乐 / 设计、图片与视觉素材` | 摄影工具与器材 | 1 | `图片、摄影与视觉资源` | `small:photography-tools` | Samyang镜头模拟器与摄影焦段视角参考 |
| 754 | `03_创作与娱乐 / 设计、图片与视觉素材` | 摄影师作品集 | 2 | `图片、摄影与视觉资源` | `small:photographer-portfolios` | 摄影师Niklas Alm个人摄影作品展示网站；Anthony Grote体育旅行与生活方式摄影作品 |
| 755 | `03_创作与娱乐 / 设计、图片与视觉素材` | 操作系统镜像 | 2 | `图片、摄影与视觉资源` | `small:operating-system-images` | Internet Archive 收录的 macOS ISO 系统镜像资源；MediaFire 托管的 macOS Sonoma 系统镜像下载文件 |
| 756 | `03_创作与娱乐 / 设计、图片与视觉素材` | 新闻与纪实摄影 | 2 | `图片、摄影与视觉资源` | `small:news-photography` | TotallyCoolPix全球实时高清新闻图片分享；《大西洋月刊》新闻纪实摄影与视觉报道图库 |
| 757 | `03_创作与娱乐 / 设计、图片与视觉素材` | 气象与地理可视化 | 1 | `图片、摄影与视觉资源` | `small:weather-visualization` | earth.nullschool 全球风场天气与海洋状况动态地图 |
| 758 | `03_创作与娱乐 / 设计、图片与视觉素材` | 汽车图片与设计 | 2 | `图片、摄影与视觉资源` | `small:automotive-images` | NetCarShow全球汽车品牌车型图片与壁纸库；Car UX汽车中控台与车载交互界面设计图库 |
| 759 | `03_创作与娱乐 / 设计、图片与视觉素材` | 照片修饰美化 | 1 | `图片、摄影与视觉资源` | `small:photo-retouching` | Pho.to在线人像美化修饰与照片润饰工具 |
| 760 | `03_创作与娱乐 / 设计、图片与视觉素材` | 照片拼贴与马赛克 | 2 | `图片、摄影与视觉资源` | `small:photo-collage` | Shape Collage 自动将照片拼接成形状图案的软件；Picartia免费在线照片马赛克拼贴图片生成器 |
| 761 | `03_创作与娱乐 / 设计、图片与视觉素材` | 科学可视化 | 2 | `图片、摄影与视觉资源` | `small:science-visualization` | 化学元素周期表动态运动可视化页面；Stuff in Space实时查看近地轨道卫星与太空物体 |
| 762 | `03_创作与娱乐 / 设计、图片与视觉素材` | 艺术摄影作品 | 2 | `图片、摄影与视觉资源` | `small:art-photography` | 1x精选高端艺术摄影作品在线图库；1x网站精选高质量艺术摄影作品画廊 |
| 763 | `03_创作与娱乐 / 设计、图片与视觉素材` | 表情包与图片素材 | 1 | `图片、摄影与视觉资源` | `small:memes-and-images` | AI 技术主题表情包图片素材浏览页面 |
| 764 | `03_创作与娱乐 / 设计、图片与视觉素材` | 视觉设计参考 | 1 | `图片、摄影与视觉资源` | `small:visual-reference` | IMP Awards历年电影海报收藏与影视视觉参考库 |
| 765 | `03_创作与娱乐 / 设计、图片与视觉素材` | 趣味图片生成 | 1 | `图片、摄影与视觉资源` | `small:fun-image-generators` | UPUP举牌小人拍立得风格趣味图片在线生成器 |
| 766 | `03_创作与娱乐 / 设计、图片与视觉素材` | 链接与图片托管 | 2 | `图片、摄影与视觉资源` | `small:link-and-image-hosting` | 品云短链接生成与跳转管理服务；品云图床图片上传与外链托管服务 |
| 767 | `03_创作与娱乐 / 设计、图片与视觉素材` | UI设计灵感 | 1 | `字体、图标、配色与界面设计` | `small:ui-design-inspiration` | UI Notes真实产品界面设计灵感案例库 |
| 768 | `03_创作与娱乐 / 设计、图片与视觉素材` | UI设计资源 | 1 | `字体、图标、配色与界面设计` | `small:ui-design-resources` | Brainwave SaaS创意应用落地页Figma设计套件 |
| 769 | `03_创作与娱乐 / 设计、图片与视觉素材` | 产品与用户体验设计 | 1 | `字体、图标、配色与界面设计` | `small:product-and-ux-design` | 互联网早读课产品设计交互体验与用户研究社区 |
| 770 | `03_创作与娱乐 / 设计、图片与视觉素材` | 前端图标资源 | 1 | `字体、图标、配色与界面设计` | `small:frontend-icons` | Font Awesome 5图标字体与CSS图标库 |
| 771 | `03_创作与娱乐 / 设计、图片与视觉素材` | 图标生成与管理工具 | 2 | `字体、图标、配色与界面设计` | `small:icon-tools` | IcoMoon 在线制作SVG图标字体与图标集工具；Icons8桌面应用离线浏览与使用图标素材 |
| 772 | `03_创作与娱乐 / 设计、图片与视觉素材` | 字体与图标资源 | 2 | `字体、图标、配色与界面设计` | `small:fonts-and-icons` | 中国高校校徽字体图标资源库；方正字库阿里健康体字体包下载页面 |
| 773 | `03_创作与娱乐 / 设计、图片与视觉素材` | 字体与排版资源 | 2 | `字体、图标、配色与界面设计` | `small:fonts` | LXGW 自制开源免费中文字体汇总；朱雀仿宋开源中文正文仿宋字体项目介绍 |
| 774 | `03_创作与娱乐 / 设计、图片与视觉素材` | 字体设计社区 | 1 | `字体、图标、配色与界面设计` | `small:font-communities` | 字体传奇网免费字体下载与中文字体设计交流 |
| 775 | `03_创作与娱乐 / 设计、图片与视觉素材` | 字体识别与管理工具 | 1 | `字体、图标、配色与界面设计` | `small:font-tools` | Fonts Ninja网页字体识别与字体工作流插件 |
| 776 | `03_创作与娱乐 / 设计、图片与视觉素材` | 网页字体服务 | 1 | `字体、图标、配色与界面设计` | `small:web-fonts` | 有字库中文网页字体WebFont在线服务平台 |
| 777 | `03_创作与娱乐 / 设计、图片与视觉素材` | 芯片与科技观点 | 1 | `字体、图标、配色与界面设计` | `small:semiconductors-and-tech` | 老石谈芯：芯片本质与高效生活方法分享 |
| 778 | `03_创作与娱乐 / 设计、图片与视觉素材` | 配色灵感与社区 | 2 | `字体、图标、配色与界面设计` | `small:color-inspiration` | 配色网色彩搭配方案交流学习与设计灵感社区；Okay Bueno 图片作品配色提取与灵感色板收藏页 |
| 779 | `03_创作与娱乐 / 设计、图片与视觉素材` | AI编程设计规范 | 2 | `设计灵感与创意资源` | `small:ai-coding-design-guides` | 面向 AI 编程代理的 DESIGN.md 设计规范合集；受 Claude 启发的 DESIGN.md 设计系统规范示例 |
| 780 | `03_创作与娱乐 / 设计、图片与视觉素材` | AI设计与品牌创作 | 1 | `设计灵感与创意资源` | `small:ai-design` | Logo-Creator 在线AI品牌标志生成器 |
| 781 | `03_创作与娱乐 / 设计、图片与视觉素材` | AI设计资源 | 1 | `设计灵感与创意资源` | `small:ai-design-resources` | MotionSites高品质AI网页首屏设计提示词素材库 |
| 782 | `03_创作与娱乐 / 设计、图片与视觉素材` | API与软件设计 | 1 | `设计灵感与创意资源` | `small:api-and-software-design` | DeepL API设计中用于限制滥用的欺骗战术分析 |
| 783 | `03_创作与娱乐 / 设计、图片与视觉素材` | H5页面设计 | 1 | `设计灵感与创意资源` | `small:h5-design` | 意派Epub360专业H5互动页面在线设计制作平台 |
| 784 | `03_创作与娱乐 / 设计、图片与视觉素材` | OCR 与计算机视觉 | 1 | `设计灵感与创意资源` | `small:ocr-and-vision` | PaddleOCR 文字识别框架快速开始开发文档 |
| 785 | `03_创作与娱乐 / 设计、图片与视觉素材` | PPT 模板与素材 | 1 | `设计灵感与创意资源` | `small:presentation-resources` | HiPPTer演示文稿模板素材资源导航 |
| 786 | `03_创作与娱乐 / 设计、图片与视觉素材` | 中国艺术与文化专题 | 2 | `设计灵感与创意资源` | `small:chinese-art` | CGTN 宋代绘画自然主题沉浸式数字专题；CGTN 宋代绘画自然主题中文互动数字专题 |
| 787 | `03_创作与娱乐 / 设计、图片与视觉素材` | 书法篆刻资料 | 1 | `设计灵感与创意资源` | `small:calligraphy-resources` | 书法字典在线书法篆刻字形学习资料平台 |
| 788 | `03_创作与娱乐 / 设计、图片与视觉素材` | 交互艺术与实验网页 | 2 | `设计灵感与创意资源` | `small:interactive-art` | 无限横向滚动的中国山水画沉浸式网页作品；Mikutap可交互音乐视觉效果网页体验 |
| 789 | `03_创作与娱乐 / 设计、图片与视觉素材` | 产品展示与样机工具 | 2 | `设计灵感与创意资源` | `small:product-design-tools` | Rotato多角度手机设备样机展示制作工具；墨刀移动应用产品原型设计制作教程 |
| 790 | `03_创作与娱乐 / 设计、图片与视觉素材` | 产品设计与协作 | 1 | `设计灵感与创意资源` | `small:product-design` | 网银项目蓝湖产品原型与设计协作页面 |
| 791 | `03_创作与娱乐 / 设计、图片与视觉素材` | 但丁与艺术评论 | 1 | `设计灵感与创意资源` | `small:dante-art-analysis` | 但丁神曲鹰意象与垂直性艺术解读文章 |
| 792 | `03_创作与娱乐 / 设计、图片与视觉素材` | 免费图库与素材检索 | 2 | `设计灵感与创意资源` | `small:free-stock-assets` | Librestock 免费图片与视频素材聚合搜索引擎；Startup Stock Photos 免费科技办公场景图库 |
| 793 | `03_创作与娱乐 / 设计、图片与视觉素材` | 写作与情书素材 | 1 | `设计灵感与创意资源` | `small:writing-resources` | 手写情书素材资源下载与情书模板网站 |
| 794 | `03_创作与娱乐 / 设计、图片与视觉素材` | 创作媒体素材 | 1 | `设计灵感与创意资源` | `small:creative-media-assets` | 爱给网游戏影视与视频后期素材下载 |
| 795 | `03_创作与娱乐 / 设计、图片与视觉素材` | 前端主题与设计 | 1 | `设计灵感与创意资源` | `small:frontend-design` | Mix Space 博客系统 Shiro 前端主题展示页面 |
| 796 | `03_创作与娱乐 / 设计、图片与视觉素材` | 前端设计与模板灵感 | 2 | `设计灵感与创意资源` | `small:frontend-design-inspiration` | NavNav 前端导航栏与 CSS 界面模板灵感库；Resn 创意数字工作室网页交互设计案例作品集 |
| 797 | `03_创作与娱乐 / 设计、图片与视觉素材` | 前端设计系统 | 1 | `设计灵感与创意资源` | `small:frontend-design-systems` | 抖音 Semi Design 企业级中后台设计系统 |
| 798 | `03_创作与娱乐 / 设计、图片与视觉素材` | 动态视觉作品 | 1 | `设计灵感与创意资源` | `small:motion-design-inspiration` | CUTTT循环动态视觉艺术短片Vimeo播放页面 |
| 799 | `03_创作与娱乐 / 设计、图片与视觉素材` | 包装设计 | 1 | `设计灵感与创意资源` | `small:packaging-design` | 包小盒在线3D包装设计与渲染工具 |
| 800 | `03_创作与娱乐 / 设计、图片与视觉素材` | 博物馆与历史 | 1 | `设计灵感与创意资源` | `small:museums-history` | 埃及文明国家博物馆古埃及木乃伊展览 |
| 801 | `03_创作与娱乐 / 设计、图片与视觉素材` | 博物馆与艺术馆藏 | 1 | `设计灵感与创意资源` | `small:museums-and-art-collections` | 巴黎市立博物馆联盟在线艺术藏品检索平台 |
| 802 | `03_创作与娱乐 / 设计、图片与视觉素材` | 品牌素材规范 | 2 | `设计灵感与创意资源` | `small:brand-assets` | Meta 官方 Facebook 品牌标志下载与使用规范；Meta 官方 Instagram 品牌素材与视觉使用指南 |
| 803 | `03_创作与娱乐 / 设计、图片与视觉素材` | 品牌设计规范 | 2 | `设计灵感与创意资源` | `small:design-guidelines` | 微信WeDesign品牌设计规范与资源下载页面；全球品牌视觉识别与设计规范文档档案库 |
| 804 | `03_创作与娱乐 / 设计、图片与视觉素材` | 在线平面设计工具 | 2 | `设计灵感与创意资源` | `small:online-design` | 稿定设计：在线制作海报、电商图片与设计模板的工具平台；创客贴在线平面设计与海报模板制作工具 |
| 805 | `03_创作与娱乐 / 设计、图片与视觉素材` | 在线绘画工具 | 1 | `设计灵感与创意资源` | `small:drawing-tools` | Google AutoDraw人工智能辅助涂鸦绘画工具 |
| 806 | `03_创作与娱乐 / 设计、图片与视觉素材` | 在线设计工具 | 1 | `设计灵感与创意资源` | `small:online-design-tools` | Crello在线平面设计模板与图片编辑工具 |
| 807 | `03_创作与娱乐 / 设计、图片与视觉素材` | 大模型 API 定价 | 2 | `设计灵感与创意资源` | `small:llm-api-pricing` | 国内外 AI 大语言模型 API 价格对比；Deepbricks AI 模型服务定价页面 |
| 808 | `03_创作与娱乐 / 设计、图片与视觉素材` | 大模型 API 文档 | 2 | `设计灵感与创意资源` | `small:llm-api-docs` | 小米 MiMo 大模型开放平台中文开发文档；阶跃星辰开放平台文本大模型使用文档 |
| 809 | `03_创作与娱乐 / 设计、图片与视觉素材` | 大模型平台 | 2 | `设计灵感与创意资源` | `small:llm-platforms` | 小米 MiMo 人工智能模型产品官网首页；商汤日日新大模型平台 Token 用量套餐页面 |
| 810 | `03_创作与娱乐 / 设计、图片与视觉素材` | 大模型硬件参考 | 1 | `设计灵感与创意资源` | `small:llm-hardware` | 大语言模型推理所需显卡显存容量排行榜 |
| 811 | `03_创作与娱乐 / 设计、图片与视觉素材` | 大模型评测 | 1 | `设计灵感与创意资源` | `small:llm-evaluation` | SuperCLUE 大模型中文能力评测与排行榜平台 |
| 812 | `03_创作与娱乐 / 设计、图片与视觉素材` | 大模型评测榜单 | 2 | `设计灵感与创意资源` | `small:llm-leaderboards` | Artificial Analysis 百款大语言模型性能对比排行榜；SuperCLUE 中文大模型能力测评基准与榜单 |
| 813 | `03_创作与娱乐 / 设计、图片与视觉素材` | 建站与网页设计 | 1 | `设计灵感与创意资源` | `small:website-builders` | Wix免费建站与网页设计平台中文主页 |
| 814 | `03_创作与娱乐 / 设计、图片与视觉素材` | 建筑与室内设计 | 1 | `设计灵感与创意资源` | `small:architecture-and-interiors` | ArchDaily全球建筑项目案例与设计资讯平台 |
| 815 | `03_创作与娱乐 / 设计、图片与视觉素材` | 当代艺术作品 | 1 | `设计灵感与创意资源` | `small:contemporary-art` | Ian Fisher持续观察与绘制天空云层的艺术项目 |
| 816 | `03_创作与娱乐 / 设计、图片与视觉素材` | 思维模型与决策工具 | 1 | `设计灵感与创意资源` | `small:thinking-and-decision-tools` | Untools帮助思考与决策的思维模型工具集合 |
| 817 | `03_创作与娱乐 / 设计、图片与视觉素材` | 插画与艺术应用 | 1 | `设计灵感与创意资源` | `small:art-and-illustration` | PivisionM Pixiv 第三方安卓客户端介绍与下载 |
| 818 | `03_创作与娱乐 / 设计、图片与视觉素材` | 插画创作社区 | 2 | `设计灵感与创意资源` | `small:illustration-communities` | drawr手绘插画创作过程与日记交流社区；Pixiv日本插画同人绘画与二次元创作社区 |
| 819 | `03_创作与娱乐 / 设计、图片与视觉素材` | 插画师与角色设计作品集 | 2 | `设计灵感与创意资源` | `small:illustration-portfolios` | 韩国角色艺术家SAKIROO插画设计作品集；Nate Williams国际插画与手写字体作品集 |
| 820 | `03_创作与娱乐 / 设计、图片与视觉素材` | 插画约稿 | 1 | `设计灵感与创意资源` | `small:illustration-commissions` | 米画师商业插画师约稿与作品浏览平台 |
| 821 | `03_创作与娱乐 / 设计、图片与视觉素材` | 数字艺术与壁纸 | 2 | `设计灵感与创意资源` | `small:digital-art` | CoolVibe数字艺术插画壁纸与灵感画廊；ArtStation全球数字艺术家作品集与招聘平台 |
| 822 | `03_创作与娱乐 / 设计、图片与视觉素材` | 数字艺术作品集 | 1 | `设计灵感与创意资源` | `small:digital-art-portfolios` | 数字插画艺术家Seerlight的SuperRare作品主页 |
| 823 | `03_创作与娱乐 / 设计、图片与视觉素材` | 文学与艺术 | 1 | `设计灵感与创意资源` | `small:literature-art` | 文学中心收录的安吉拉卡特主题同人艺术作品 |
| 824 | `03_创作与娱乐 / 设计、图片与视觉素材` | 智慧城市与数字政府 | 1 | `设计灵感与创意资源` | `small:smart-city-research` | 智慧城市数字政府数字经济与数据要素行业分析平台 |
| 825 | `03_创作与娱乐 / 设计、图片与视觉素材` | 本地大模型 | 2 | `设计灵感与创意资源` | `small:local-ai` | GPT4All 本地运行开源大语言模型平台；Qwen-7B 通义千问本地部署与 Chatbox 使用教程 |
| 826 | `03_创作与娱乐 / 设计、图片与视觉素材` | 沉浸式数字体验 | 1 | `设计灵感与创意资源` | `small:immersive-digital-experiences` | 太一灵境奇幻世界沉浸式体验官方网站 |
| 827 | `03_创作与娱乐 / 设计、图片与视觉素材` | 矢量设计素材 | 1 | `设计灵感与创意资源` | `small:vector-design-assets` | 懒人图库矢量中国传统风格设计素材专题 |
| 828 | `03_创作与娱乐 / 设计、图片与视觉素材` | 私有大模型平台 | 1 | `设计灵感与创意资源` | `small:private-llm-platforms` | AI Planet面向个人与企业的私有安全大模型平台 |
| 829 | `03_创作与娱乐 / 设计、图片与视觉素材` | 网站设计与搭建 | 2 | `设计灵感与创意资源` | `small:web-design-tools` | Webflow 可视化自定义网站设计与构建平台；Pixso AI 原生 UI 设计协作与代码生成工具 |
| 830 | `03_创作与娱乐 / 设计、图片与视觉素材` | 网页设计资源 | 1 | `设计灵感与创意资源` | `small:web-design-resources` | 大学生静态网页设计作业模板资源 |
| 831 | `03_创作与娱乐 / 设计、图片与视觉素材` | 艺术交易与画廊 | 1 | `设计灵感与创意资源` | `small:art-marketplaces` | Singulart线上画廊与原创当代艺术作品购买平台 |
| 832 | `03_创作与娱乐 / 设计、图片与视觉素材` | 艺术创作社区 | 1 | `设计灵感与创意资源` | `small:art-communities` | DeviantArt全球插画创作作品展示与艺术社区 |
| 833 | `03_创作与娱乐 / 设计、图片与视觉素材` | 艺术品与文物图库 | 2 | `设计灵感与创意资源` | `small:art-collections` | 中华珍宝馆中国传统艺术品与文物高清图库；芝加哥艺术博物馆公共领域藏品在线检索库 |
| 834 | `03_创作与娱乐 / 设计、图片与视觉素材` | 艺术家作品集 | 2 | `设计灵感与创意资源` | `small:artist-portfolios` | 蒸汽波视觉艺术家Magdiel Lopez个人作品网站；Beeple数字艺术作品与C4D创作资源网站 |
| 835 | `03_创作与娱乐 / 设计、图片与视觉素材` | 艺术教育院校 | 1 | `设计灵感与创意资源` | `small:art-education` | 中央美术学院官方网站与招生艺术资讯 |
| 836 | `03_创作与娱乐 / 设计、图片与视觉素材` | 营销页面设计 | 1 | `设计灵感与创意资源` | `small:marketing-design` | MAKA免费H5微场景与移动营销页面制作平台 |
| 837 | `03_创作与娱乐 / 设计、图片与视觉素材` | 落地页设计与转化工具 | 1 | `设计灵感与创意资源` | `small:landing-page-tools` | 落地页设计开发与优化工具百项检查清单 |
| 838 | `03_创作与娱乐 / 设计、图片与视觉素材` | 虚拟博物馆导览 | 1 | `设计灵感与创意资源` | `small:virtual-museums` | 毛里茨皇家美术馆线上360度高清虚拟导览 |
| 839 | `03_创作与娱乐 / 设计、图片与视觉素材` | 表情包与图片素材 | 1 | `设计灵感与创意资源` | `small:meme-resources` | DIY斗图收录的海绵宝宝系列表情包图片合集 |
| 840 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计与创意作品 | 1 | `设计灵感与创意资源` | `small:design-and-creative-work` | 心乐设计个人站点与创意作品内容展示 |
| 841 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计与原型协作 | 2 | `设计灵感与创意资源` | `small:design-collaboration` | 产品大牛原型托管与设计标注协作平台；tldraw 开源无限画布与协作白板工具 |
| 842 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计与原型工具 | 1 | `设计灵感与创意资源` | `small:design-and-prototyping` | Visily AI 产品原型设计与界面线框图协作工具 |
| 843 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计作品集 | 1 | `设计灵感与创意资源` | `small:design-portfolios` | 锤子科技设计师Sandor的Dribbble设计作品主页 |
| 844 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计创作社区 | 2 | `设计灵感与创意资源` | `small:design-communities` | 视觉ME设计师与插画师作品交流社区；UI中国专业用户体验与界面设计交流平台 |
| 845 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计实用工具 | 1 | `设计灵感与创意资源` | `small:design-utilities` | Gridzzly自定义网格纸在线生成工具 |
| 846 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计标注与开发交付 | 1 | `设计灵感与创意资源` | `small:design-handoff` | 马克鳗设计稿尺寸色值标注与测量工具 |
| 847 | `03_创作与娱乐 / 设计、图片与视觉素材` | 设计资源导航 | 1 | `设计灵感与创意资源` | `small:design-resource-guides` | 国外设计素材网站与矢量PSD图标资源导航 |
| 848 | `03_创作与娱乐 / 设计、图片与视觉素材` | 反应与视觉测试 | 2 | `设计特效、模板与素材` | `small:reaction-tests` | Human Benchmark在线反应时间与速度测试；日文在线动态视力测定与视觉反应测试 |
| 849 | `03_创作与娱乐 / 设计、图片与视觉素材` | 故障艺术特效 | 2 | `设计特效、模板与素材` | `small:glitch-effects` | PhotoMosh在线故障艺术影像特效制作工具；MOSH在线制作数据故障风格图片与动画工具 |
| 850 | `04_工作与生活 / 办公、效率与知识管理` | 备份与云存储 | 1 | `云存储、文件与协作` | `small:backup-storage` | 云朵备份个人数据云端备份与恢复服务 |
| 851 | `04_工作与生活 / 办公、效率与知识管理` | 工作效率工具 | 1 | `任务、日程与效率自动化` | `small:work-productivity` | 在线自动生成工作周报内容的周报生成器 |
| 852 | `04_工作与生活 / 办公、效率与知识管理` | 待办与任务管理 | 1 | `任务、日程与效率自动化` | `small:task-management` | TickTick 滴答清单安卓高级版任务管理应用下载 |
| 853 | `04_工作与生活 / 办公、效率与知识管理` | 效率与时间管理 | 1 | `任务、日程与效率自动化` | `small:productivity` | 币安创始人赵长鹏分享个人时间管理法则 |
| 854 | `04_工作与生活 / 办公、效率与知识管理` | 效率工具与桌面应用 | 2 | `任务、日程与效率自动化` | `small:productivity-tools` | uTools 新一代桌面效率工具平台官方网站；Forest专注计时与效率培养应用网站 |
| 855 | `04_工作与生活 / 办公、效率与知识管理` | 内部项目文档 | 1 | `办公效率综合工具` | `small:internal-project-docs` | 新国结EFramework项目TAPD文档文件列表 |
| 856 | `04_工作与生活 / 办公、效率与知识管理` | 协作工作空间 | 1 | `办公效率综合工具` | `small:collaboration-workspaces` | new.space 未命名在线空间页面 |
| 857 | `04_工作与生活 / 办公、效率与知识管理` | 在线协作工具 | 2 | `办公效率综合工具` | `small:online-collaboration` | Hyperbeam 在线协作浏览器与共同观看应用；Hyperbeam Wandering Shadow 在线共享房间 |
| 858 | `04_工作与生活 / 办公、效率与知识管理` | 桌面效率工具 | 1 | `办公效率综合工具` | `small:desktop-utilities` | PixPin 截图贴图长截图文字识别工具官网 |
| 859 | `04_工作与生活 / 办公、效率与知识管理` | 流程图与协作绘图 | 1 | `办公效率综合工具` | `small:diagramming-tools` | ProcessOn免费在线绘图与实时协作平台 |
| 860 | `04_工作与生活 / 办公、效率与知识管理` | 笔记本评测 | 1 | `办公效率综合工具` | `small:laptop-reviews` | DXOMARK笔记本电脑屏幕性能与产品测评列表 |
| 861 | `04_工作与生活 / 办公、效率与知识管理` | 笔记本购买 | 2 | `办公效率综合工具` | `small:laptop-shopping` | 天猫联想小新 Pro16 2023 锐龙笔记本商品页；Best Buy美国官网MacBook Air M3搜索结果页面 |
| 862 | `04_工作与生活 / 办公、效率与知识管理` | 软件工程与协作 | 1 | `办公效率综合工具` | `small:software-engineering` | GitHub公司内部培训分享与工程实践资料 |
| 863 | `04_工作与生活 / 办公、效率与知识管理` | 邮箱与账户服务 | 1 | `办公效率综合工具` | `small:email-accounts` | Yahoo日本邮箱网页版邮件收发界面 |
| 864 | `04_工作与生活 / 办公、效率与知识管理` | 邮箱与通信服务 | 1 | `办公效率综合工具` | `small:email-services` | Roundcube 网页邮箱收件箱登录页面 |
| 865 | `04_工作与生活 / 办公、效率与知识管理` | PDF 与文档工具 | 2 | `文档、办公与演示材料` | `small:pdf-and-documents` | ABBYY 泰比扫描 PDF 文字识别与格式转换软件；Smallpdf 账户个人资料与订阅账单管理页面 |
| 866 | `04_工作与生活 / 办公、效率与知识管理` | 写作与文档工具 | 1 | `文档、办公与演示材料` | `small:writing-and-document-tools` | Typora所见即所得Markdown写作与阅读编辑器官网 |
| 867 | `04_工作与生活 / 办公、效率与知识管理` | 办公与文档工具 | 2 | `文档、办公与演示材料` | `small:office-productivity` | 煦航毕业论文与Word文档自动排版软件官网；Office Tool Plus微软Office部署管理工具下载 |
| 868 | `04_工作与生活 / 办公、效率与知识管理` | 办公协作工具 | 1 | `文档、办公与演示材料` | `small:office-and-collaboration` | 石墨文档电脑移动端协同办公客户端下载 |
| 869 | `04_工作与生活 / 办公、效率与知识管理` | 办公文档模板 | 1 | `文档、办公与演示材料` | `small:office-templates` | 稻壳儿Docer办公文档简历与演示模板资源 |
| 870 | `04_工作与生活 / 办公、效率与知识管理` | 文档处理与解析工具 | 2 | `文档、办公与演示材料` | `small:document-tools` | 395印章在线生成电子印章与制作印章图片工具；百度文库文档在线解析与内容复制工具 |
| 871 | `04_工作与生活 / 办公、效率与知识管理` | 文档处理工具 | 1 | `文档、办公与演示材料` | `small:document-processing` | pdf2docx将PDF文档转换为Word的Python文档 |
| 872 | `04_工作与生活 / 办公、效率与知识管理` | 文档站与静态站点工具 | 1 | `文档、办公与演示材料` | `small:documentation-tools` | VirePressSimple 基于 VitePress 的简洁配置模板 |
| 873 | `04_工作与生活 / 办公、效率与知识管理` | 演示文稿分享 | 2 | `文档、办公与演示材料` | `small:presentation-sharing` | PPT加行业报告与语音演示内容分享平台；SlideShare 全球专业幻灯片与知识分享平台 |
| 874 | `04_工作与生活 / 办公、效率与知识管理` | 纪录片资源 | 1 | `文档、办公与演示材料` | `small:documentaries` | IHaveNoTV免费在线纪录片在线观看平台 |
| 875 | `04_工作与生活 / 办公、效率与知识管理` | Notion模板 | 2 | `笔记、知识管理与书签整理` | `small:notion-templates` | Notion高级旅行计划模板中文版页面；Easlo的Notion效率模板与数字产品商店 |
| 876 | `04_工作与生活 / 办公、效率与知识管理` | Obsidian资源 | 1 | `笔记、知识管理与书签整理` | `small:obsidian-resources` | Obsidian社区2024年度插件与主题宝石获奖名单 |
| 877 | `04_工作与生活 / 办公、效率与知识管理` | 个人知识库与笔记 | 1 | `笔记、知识管理与书签整理` | `small:personal-knowledge-base` | Wolai协作空间中的aaa001主页面知识库文档 |
| 878 | `04_工作与生活 / 办公、效率与知识管理` | 个人记录与知识管理 | 1 | `笔记、知识管理与书签整理` | `small:personal-knowledge` | MemoTrace 留痕：个人信息记录与回忆管理工具 |
| 879 | `04_工作与生活 / 办公、效率与知识管理` | 博客与知识记录 | 1 | `笔记、知识管理与书签整理` | `small:blogs-and-knowledge` | 知录网站文章与知识记录内容分类浏览页面 |
| 880 | `04_工作与生活 / 办公、效率与知识管理` | 知识查询与计算工具 | 1 | `笔记、知识管理与书签整理` | `small:knowledge-tools` | Wolfram Alpha：面向计算、知识查询与数据分析的知识引擎 |
| 881 | `04_工作与生活 / 办公、效率与知识管理` | 知识百科与查询 | 1 | `笔记、知识管理与书签整理` | `small:knowledge-reference` | 互动百科中文百科知识查询平台 |
| 882 | `04_工作与生活 / 办公、效率与知识管理` | 知识问答社区 | 1 | `笔记、知识管理与书签整理` | `small:knowledge-communities` | Quora 国际知识问答与观点讨论社区 |
| 883 | `04_工作与生活 / 办公、效率与知识管理` | 笔记与内容管理 | 1 | `笔记、知识管理与书签整理` | `small:notes-and-organization` | xTiles 内容整理笔记管理与项目协作一体化工具 |
| 884 | `04_工作与生活 / 办公、效率与知识管理` | 笔记与写作工具 | 2 | `笔记、知识管理与书签整理` | `small:notes-and-writing` | 我来 Wolai 云端协作与个人笔记客户端下载；幕布 App 多平台大纲笔记客户端下载页面 |
| 885 | `04_工作与生活 / 办公、效率与知识管理` | 笔记与知识管理 | 2 | `笔记、知识管理与书签整理` | `small:note-taking-tools` | NotePal微信读书笔记导入与整理工具；Montaigne面向苹果设备的笔记应用服务 |
| 886 | `04_工作与生活 / 办公、效率与知识管理` | 笔记与知识管理 | 2 | `笔记、知识管理与书签整理` | `small:notes-and-knowledge` | Ideanote 在线灵感记录与创意笔记工具；Scrintal 结合笔记管理和思维导图的白板工具 |
| 887 | `04_工作与生活 / 商业、营销与电商` | AI 商业研究与洞察 | 1 | `商业、产品与行业研究` | `small:ai-business-research` | BeeBee人工智能商业洞察分析平台 |
| 888 | `04_工作与生活 / 商业、营销与电商` | 上市公司信息披露 | 1 | `商业、产品与行业研究` | `small:listed-company-disclosures` | 巨潮资讯网上市公司公告定期报告与信息披露平台 |
| 889 | `04_工作与生活 / 商业、营销与电商` | 企业与品牌官网 | 2 | `商业、产品与行业研究` | `small:business-websites` | 言安堂生物科技与健康相关品牌官网；思通数科：信息技术与数据智能服务官网 |
| 890 | `04_工作与生活 / 商业、营销与电商` | 企业与客户管理软件 | 1 | `商业、产品与行业研究` | `small:business-software` | Twenty 开源客户关系管理 CRM 系统官网 |
| 891 | `04_工作与生活 / 商业、营销与电商` | 企业官网 | 2 | `商业、产品与行业研究` | `small:company-websites` | 武汉思维跳跃科技有限公司企业官方网站主页；MonTAG品牌或企业官方网站主页入口 |
| 892 | `04_工作与生活 / 商业、营销与电商` | 企业工商信息 | 1 | `商业、产品与行业研究` | `small:business-information` | 博联众科（武汉）科技有限公司工商信用信息 |
| 893 | `04_工作与生活 / 商业、营销与电商` | 企业管理工具 | 2 | `商业、产品与行业研究` | `small:business-management-tools` | 织蝶云企业OA项目与客户关系管理平台；TinkMail 企业版套餐与服务计划说明文档 |
| 894 | `04_工作与生活 / 商业、营销与电商` | 公司与团队信息 | 1 | `商业、产品与行业研究` | `small:company-profiles` | 润去新加坡平台运营公司与团队介绍页面 |
| 895 | `04_工作与生活 / 商业、营销与电商` | 创业与产品机会 | 1 | `商业、产品与行业研究` | `small:startup-resources` | StartZone：发现可盈利创业项目与初创公司机会 |
| 896 | `04_工作与生活 / 商业、营销与电商` | 创业与企业管理 | 1 | `商业、产品与行业研究` | `small:startup-operations` | Captable 开源股权表与公司融资管理平台 |
| 897 | `04_工作与生活 / 商业、营销与电商` | 商业与客户管理工具 | 1 | `商业、产品与行业研究` | `small:business-tools` | 客户无忧手机版客户关系管理应用下载 |
| 898 | `04_工作与生活 / 商业、营销与电商` | 市场规模测算 | 1 | `商业、产品与行业研究` | `small:market-sizing` | 不同行业潜在市场空间测算方法与分析框架 |
| 899 | `04_工作与生活 / 商业、营销与电商` | 预测市场 | 1 | `商业、产品与行业研究` | `small:prediction-markets` | Polymarket中文站：全球事件预测市场交易平台 |
| 900 | `04_工作与生活 / 商业、营销与电商` | 企业官网与介绍 | 1 | `商业运营与服务平台` | `small:corporate-profiles` | 阿拉丁控股集团有限公司官网与企业介绍 |
| 901 | `04_工作与生活 / 商业、营销与电商` | 企业应用项目 | 1 | `商业运营与服务平台` | `small:enterprise-projects` | 微人事Java人力资源管理项目文档 |
| 902 | `04_工作与生活 / 商业、营销与电商` | 企业服务平台 | 1 | `商业运营与服务平台` | `small:enterprise-service-platforms` | 鹏云网络企业服务平台主界面 |
| 903 | `04_工作与生活 / 商业、营销与电商` | 企业榜单与指数 | 1 | `商业运营与服务平台` | `small:rankings-and-indices` | 胡润研究院中国财富榜单与行业指数报告 |
| 904 | `04_工作与生活 / 商业、营销与电商` | 企业科技行业报告 | 1 | `商业运营与服务平台` | `small:enterprise-tech-reports` | T媒体企业数字化与科技行业研究报告库 |
| 905 | `04_工作与生活 / 商业、营销与电商` | 信息技术企业 | 2 | `商业运营与服务平台` | `small:technology-companies` | 统信国基官方网站与信息技术服务介绍；智科集团上海广东成都山东业务与机构介绍 |
| 906 | `04_工作与生活 / 商业、营销与电商` | 医疗科技企业 | 1 | `商业运营与服务平台` | `small:healthcare-companies` | 贝医科技医疗健康技术服务官方网站 |
| 907 | `04_工作与生活 / 商业、营销与电商` | 咨询与商业洞察 | 2 | `商业运营与服务平台` | `small:consulting-insights` | 普华永道中国行业洞察与专业服务研究报告；麦肯锡中国洞见文章与麦肯锡季刊研究内容 |
| 908 | `04_工作与生活 / 商业、营销与电商` | 外勤销售管理 | 1 | `商业运营与服务平台` | `small:sales-field-management` | 聘聘云拜访通外勤拜访管理系统入口 |
| 909 | `04_工作与生活 / 商业、营销与电商` | 客户关系管理工具 | 1 | `商业运营与服务平台` | `small:crm-tools` | Cordys开源AI CRM客户关系管理系统与私有化部署 |
| 910 | `04_工作与生活 / 商业、营销与电商` | 客服与帮助台工具 | 1 | `商业运营与服务平台` | `small:customer-support-tools` | Intercom 面向 AI 智能体时代的客服帮助台平台 |
| 911 | `04_工作与生活 / 商业、营销与电商` | 工业企业信息 | 2 | `商业运营与服务平台` | `small:industrial-companies` | 河南液压气动公司简介与企业信息页面；北重集团官方网站与重型装备企业信息 |
| 912 | `04_工作与生活 / 商业、营销与电商` | 房产运营工具 | 2 | `商业运营与服务平台` | `small:real-estate-tools` | 推房神器房源采集群发与刷新工具登录页面；链家厦门商业贷款与公积金房贷计算器工具 |
| 913 | `04_工作与生活 / 商业、营销与电商` | 房地产企业信息 | 1 | `商业运营与服务平台` | `small:real-estate-companies` | 保利地产投资顾问有限公司企业介绍页面 |
| 914 | `04_工作与生活 / 商业、营销与电商` | 新媒体运营 | 2 | `商业运营与服务平台` | `small:new-media-operations` | 新榜内容创业账号运营与新媒体数据服务平台；考拉新媒体导航收录运营营销工具与行业资源 |
| 915 | `04_工作与生活 / 商业、营销与电商` | 直播电商与 MCN | 1 | `商业运营与服务平台` | `small:live-commerce` | 珀箫文化直播带货 MCN 与政务媒体直播服务 |
| 916 | `04_工作与生活 / 商业、营销与电商` | 直播电商企业 | 1 | `商业运营与服务平台` | `small:live-commerce-companies` | 遥望科技直播电商与新媒体营销业务官网 |
| 917 | `04_工作与生活 / 商业、营销与电商` | 科技新闻与创业资讯 | 1 | `商业运营与服务平台` | `small:tech-news` | Hacker News科技创业新闻讨论社区 |
| 918 | `04_工作与生活 / 商业、营销与电商` | 腕表品牌资料 | 1 | `商业运营与服务平台` | `small:watch-reference` | 腕表之家全球男女手表品牌标志与资料大全 |
| 919 | `04_工作与生活 / 商业、营销与电商` | 财经商业资讯 | 1 | `商业运营与服务平台` | `small:financial-news` | 21经济网中国财经新闻与商业经济资讯门户 |
| 920 | `04_工作与生活 / 商业、营销与电商` | 邮件客户端 | 1 | `商业运营与服务平台` | `small:email-tools` | Foxmail Windows版邮件客户端官方下载 |
| 921 | `04_工作与生活 / 商业、营销与电商` | 产品增长与营销 | 1 | `电商、商品与供应链` | `small:product-growth` | Pushkeen.AI：浏览优秀应用推送通知与增长案例 |
| 922 | `04_工作与生活 / 商业、营销与电商` | 产品管理与协作工具 | 1 | `电商、商品与供应链` | `small:product-management-tools` | Polyscore面向产品开发团队的智能评估与协作平台 |
| 923 | `04_工作与生活 / 商业、营销与电商` | 产品管理学习资源 | 1 | `电商、商品与供应链` | `small:product-management-learning` | Lenny产品经理通讯订阅会员与学习资源通行证 |
| 924 | `04_工作与生活 / 商业、营销与电商` | 创业与产品增长 | 1 | `电商、商品与供应链` | `small:startup-product` | Instagram早期创业历程与创始人特质分析 |
| 925 | `04_工作与生活 / 商业、营销与电商` | 品牌与选购指南 | 1 | `电商、商品与供应链` | `small:shopping-guides` | 买购网品牌排行与消费知识指南 |
| 926 | `04_工作与生活 / 商业、营销与电商` | 商品热销排行 | 1 | `电商、商品与供应链` | `small:shopping-rankings` | 拼多多数码品类今日热销商品排行榜数据页面 |
| 927 | `04_工作与生活 / 商业、营销与电商` | 批发采购货源 | 1 | `电商、商品与供应链` | `small:wholesale-sourcing` | 阿里巴巴1688批发采购与货源交易平台 |
| 928 | `04_工作与生活 / 商业、营销与电商` | 电商营销工具 | 1 | `电商、商品与供应链` | `small:ecommerce-marketing` | 天猫云码营销引擎账号登录与营销管理入口 |
| 929 | `04_工作与生活 / 商业、营销与电商` | 电商零售产业资讯 | 2 | `电商、商品与供应链` | `small:ecommerce-industry` | 亿邦动力电商产业资讯研究与商业观察平台；星图数据电商消费热点与零售市场洞察平台 |
| 930 | `04_工作与生活 / 商业、营销与电商` | 综合电商购物 | 1 | `电商、商品与供应链` | `small:general-shopping` | 天猫综合零售购物与品牌商品交易平台 |
| 931 | `04_工作与生活 / 商业、营销与电商` | SEO 与网站运营 | 1 | `营销、广告与品牌增长` | `small:seo-and-webmaster` | 桔子SEO网站长搜索引擎优化经验分享平台 |
| 932 | `04_工作与生活 / 商业、营销与电商` | SEO与站长工具 | 2 | `营销、广告与品牌增长` | `small:seo-webmaster` | Bing Webmaster Tools站长工具介绍；Bing站长工具网站所有权验证页面 |
| 933 | `04_工作与生活 / 商业、营销与电商` | SEO与网站运营 | 1 | `营销、广告与品牌增长` | `small:seo-marketing` | Ahrefs面向初学者的搜索引擎优化完整指南 |
| 934 | `04_工作与生活 / 商业、营销与电商` | 内容营销案例 | 1 | `营销、广告与品牌增长` | `small:content-marketing` | 剧星传媒视频内容整合营销案例与服务展示 |
| 935 | `04_工作与生活 / 商业、营销与电商` | 医药健康品牌 | 1 | `营销、广告与品牌增长` | `small:health-brands` | 三浦制药日本健康医药企业官方网站 |
| 936 | `04_工作与生活 / 商业、营销与电商` | 品牌网站案例 | 2 | `营销、广告与品牌增长` | `small:brand-websites` | Enod 法国餐饮品牌展示网站与视觉案例；LINDBERG 高端丹麦眼镜品牌中国官方网站 |
| 937 | `04_工作与生活 / 商业、营销与电商` | 广告营销案例 | 1 | `营销、广告与品牌增长` | `small:marketing-cases` | 数英网品牌广告营销项目与创意案例库 |
| 938 | `04_工作与生活 / 商业、营销与电商` | 美容护肤品牌 | 2 | `营销、广告与品牌增长` | `small:beauty-brands` | 碧妍思BIOYONCE品牌产品介绍页面；德玛贝尔美容护肤品牌的官方网站与品牌介绍 |
| 939 | `04_工作与生活 / 商业、营销与电商` | 营销工具与服务 | 2 | `营销、广告与品牌增长` | `small:marketing-tools-and-services` | 鱼爪传媒网络推广与社交视频营销服务平台；addog广告营销从业者常用工具与资源导航 |
| 940 | `04_工作与生活 / 商业、营销与电商` | 营销推广工具 | 2 | `营销、广告与品牌增长` | `small:marketing-tools` | 私域引流宝开源推广工具控制台首页；私域引流宝开源推广工具登录页面 |
| 941 | `04_工作与生活 / 商业、营销与电商` | 营销资讯与案例 | 1 | `营销、广告与品牌增长` | `small:marketing-news-and-cases` | 梅花网营销传播资讯与品牌营销案例数据库 |
| 942 | `04_工作与生活 / 生活、出行与消费` | 汽车选购与预订 | 1 | `住房、家居与车辆` | `small:car-shopping` | 梅赛德斯奔驰车型意向金在线预订订单页面 |
| 943 | `04_工作与生活 / 生活、出行与消费` | 深圳租房住房 | 1 | `住房、家居与车辆` | `small:shenzhen-housing` | 深圳万科泊寓公寓房源与整租出租查询 |
| 944 | `04_工作与生活 / 生活、出行与消费` | 租房与房源搜索 | 1 | `住房、家居与车辆` | `small:housing-search` | 杭州多平台地图找房租房房源聚合检索引擎 |
| 945 | `04_工作与生活 / 生活、出行与消费` | 租房与房源搜索 | 1 | `住房、家居与车辆` | `small:rentals-housing` | 自如上海地图找房与租房房源搜索页面 |
| 946 | `04_工作与生活 / 生活、出行与消费` | 健康与护肤产品 | 2 | `健康、家庭与兴趣生活` | `small:health-and-skincare` | Romega鲱鱼卵Omega-3营养补充产品介绍；My Green Herbs修丽可护肤产品系列商城 |
| 947 | `04_工作与生活 / 生活、出行与消费` | 健康医疗服务 | 1 | `健康、家庭与兴趣生活` | `small:health-services` | 厦门市民健康管理个人服务平台入口 |
| 948 | `04_工作与生活 / 生活、出行与消费` | 医疗健康信息 | 1 | `健康、家庭与兴趣生活` | `small:health-information` | 国家权威中国医药信息与药品疾病查询平台 |
| 949 | `04_工作与生活 / 生活、出行与消费` | 医疗服务 | 2 | `健康、家庭与兴趣生活` | `small:healthcare-services` | 四川大学华西医院官方网站首页入口；北京协和医院官方网站首页入口 |
| 950 | `04_工作与生活 / 生活、出行与消费` | 医疗科技资讯 | 1 | `健康、家庭与兴趣生活` | `small:health-tech` | 中山医大携手微软培育智慧医疗人才新闻报道 |
| 951 | `04_工作与生活 / 生活、出行与消费` | 美食与烹饪 | 1 | `健康、家庭与兴趣生活` | `small:food-recipes` | Allrecipes全球家常菜谱与烹饪灵感网站 |
| 952 | `04_工作与生活 / 生活、出行与消费` | 计算机技能竞赛 | 2 | `健康、家庭与兴趣生活` | `small:computer-competitions` | 2023年第六届大学生计算机技能应用大赛官网；大学生计算机技能应用大赛决赛入围名单公示 |
| 953 | `04_工作与生活 / 生活、出行与消费` | 地图与世界观生成 | 1 | `旅行、通信与出行服务` | `small:map-generators` | Azgaar奇幻世界地图随机生成器 |
| 954 | `04_工作与生活 / 生活、出行与消费` | 天气与地图 | 1 | `旅行、通信与出行服务` | `small:weather-and-maps` | Windy全球风力地图与天气预报查询平台 |
| 955 | `04_工作与生活 / 生活、出行与消费` | 旅行与旅居服务 | 1 | `旅行、通信与出行服务` | `small:travel-and-living` | 明日故乡中国城市旅居数据筛选查询工具 |
| 956 | `04_工作与生活 / 生活、出行与消费` | 旅行攻略社区 | 1 | `旅行、通信与出行服务` | `small:travel-guides` | 穷游网自由行攻略与旅行经验分享社区 |
| 957 | `04_工作与生活 / 生活、出行与消费` | 虚拟旅行与环境体验 | 1 | `旅行、通信与出行服务` | `small:virtual-travel` | Drive and Listen：边开车边聆听世界城市声音 |
| 958 | `04_工作与生活 / 生活、出行与消费` | 成人交友与社交 | 2 | `日常生活与消费参考` | `small:adult-dating` | Kamii开放式关系成人交友平台首页；名媛圈全国线下约会交友平台入口 |
| 959 | `04_工作与生活 / 生活、出行与消费` | 成人夜生活信息 | 1 | `日常生活与消费参考` | `small:adult-nightlife-info` | 香港夜生活成人服务信息与资料目录网站 |
| 960 | `04_工作与生活 / 生活、出行与消费` | 搜索与地图导航 | 2 | `日常生活与消费参考` | `small:search-and-navigation` | Google香港中文版网页搜索入口；高德地图在线地图与路线导航服务 |
| 961 | `04_工作与生活 / 生活、出行与消费` | 日常实用小工具 | 2 | `日常生活与消费参考` | `small:daily-life-tools` | 911查询人民币金额数字转中文大写工具；中国亲戚关系称谓计算器与家族关系查询 |
| 962 | `04_工作与生活 / 生活、出行与消费` | 日本生活与迁居 | 1 | `日常生活与消费参考` | `small:japan-living` | 东京都面向外国居民的教育支持与入学生活指南 |
| 963 | `04_工作与生活 / 生活、出行与消费` | 时尚与生活方式 | 2 | `日常生活与消费参考` | `small:fashion-and-lifestyle` | 时尚网高端白领生活方式与潮流资讯平台；东京Style Arena日本街头时尚穿搭观察网站 |
| 964 | `04_工作与生活 / 生活、出行与消费` | 本地生活社区 | 1 | `日常生活与消费参考` | `small:local-communities` | 洛杉矶华人本地资讯分类信息交流平台 |
| 965 | `04_工作与生活 / 生活、出行与消费` | 汽车改装工具 | 1 | `日常生活与消费参考` | `small:auto-tools` | 3DTuning在线三维汽车改装与车型定制工具 |
| 966 | `04_工作与生活 / 生活、出行与消费` | 海外生活与迁居 | 2 | `日常生活与消费参考` | `small:overseas-living` | 赴新加坡落地后三个月安顿生活实用指南；个人海外迁居与肉身翻墙过程记录上篇 |
| 967 | `04_工作与生活 / 生活、出行与消费` | 深圳生活指南 | 1 | `日常生活与消费参考` | `small:shenzhen-living` | 深圳各区图书馆地址服务与借阅攻略汇总 |
| 968 | `04_工作与生活 / 生活、出行与消费` | 物流快递服务 | 2 | `日常生活与消费参考` | `small:logistics` | 顺丰速运运单SF3156001161130物流查询；快递100快递查询寄件与物流管理平台 |
| 969 | `04_工作与生活 / 生活、出行与消费` | 生活成本与城市比较 | 1 | `日常生活与消费参考` | `small:cost-of-living` | Numbeo全球城市生活成本比较计算工具 |
| 970 | `04_工作与生活 / 生活、出行与消费` | 生活技巧指南 | 1 | `日常生活与消费参考` | `small:life-guides` | 中文wikiHow生活技能与实用指南首页 |
| 971 | `04_工作与生活 / 生活、出行与消费` | 生活技能与操作指南 | 1 | `日常生活与消费参考` | `small:how-to-guides` | wikiHow生活技能与操作方法图文指南 |
| 972 | `04_工作与生活 / 生活、出行与消费` | 生活方式与个人成长 | 1 | `日常生活与消费参考` | `small:lifestyle-growth` | Zen Habits极简生活习惯与个人成长博客 |
| 973 | `04_工作与生活 / 生活、出行与消费` | 程序化生成与地图 | 1 | `日常生活与消费参考` | `small:procedural-generation` | Watabou Procgen Arcana 程序化地图生成器合集 |
| 974 | `04_工作与生活 / 生活、出行与消费` | 美国生活与城市 | 1 | `日常生活与消费参考` | `small:us-living` | MoveMap按生活偏好筛选比较美国宜居城市工具 |
| 975 | `04_工作与生活 / 生活、出行与消费` | 营养与健康 | 1 | `日常生活与消费参考` | `small:nutrition` | FatSecret中国食物热量与营养成分查询 |
| 976 | `04_工作与生活 / 生活、出行与消费` | 香港生活与证件 | 1 | `日常生活与消费参考` | `small:hong-kong-living` | 内地驾照免试换领香港十年正式驾照办理攻略 |
| 977 | `04_工作与生活 / 生活、出行与消费` | Apple订单管理 | 1 | `购物、订阅与生活服务` | `small:apple-account-orders` | Apple中国大陆商城订单详情查询页面 |
| 978 | `04_工作与生活 / 生活、出行与消费` | Spotify 订阅与开通 | 2 | `购物、订阅与生活服务` | `small:spotify-subscriptions` | 小猫 Music 巴西区 Spotify 高级会员订阅商品页；Spotify 菲律宾区订阅开通与充值使用教程 |
| 979 | `04_工作与生活 / 生活、出行与消费` | 商城与购物 | 1 | `购物、订阅与生活服务` | `small:shopping` | 老K自营店 商品分类与自营资源商城 |
| 980 | `04_工作与生活 / 生活、出行与消费` | 在线购物 | 1 | `购物、订阅与生活服务` | `small:online-shopping` | 亚马逊中国在线购物商城官方网站首页 |
| 981 | `04_工作与生活 / 生活、出行与消费` | 成人用品购物 | 1 | `购物、订阅与生活服务` | `small:adult-shopping` | 花语人形 XT DOLL 硅胶仿真人偶产品商城 |
| 982 | `04_工作与生活 / 生活、出行与消费` | 本地生活与账户服务 | 2 | `购物、订阅与生活服务` | `small:local-services` | 宁波市自来水有限公司官方网站与供水服务；58同城个人用户中心与账户管理入口 |
| 983 | `04_工作与生活 / 生活、出行与消费` | 本地生活服务 | 1 | `购物、订阅与生活服务` | `small:local-life-services` | 大众点评杭州美食生活团购与旅游服务 |
| 984 | `04_工作与生活 / 生活、出行与消费` | 汽车服务 | 2 | `购物、订阅与生活服务` | `small:auto-services` | 车质网汽车质量投诉处理与车型口碑评价平台；瓜子二手车移动端车辆买卖与估价服务平台 |
| 985 | `04_工作与生活 / 生活、出行与消费` | 海外订阅服务指南 | 2 | `购物、订阅与生活服务` | `small:subscription-guides` | 土耳其区 iCloud 服务订阅与上车操作指南；Microsoft Office 365 A 系列订阅与激活说明 |
| 986 | `04_工作与生活 / 生活、出行与消费` | 海外购物攻略 | 2 | `购物、订阅与生活服务` | `small:overseas-shopping-guides` | 2023年美国亚马逊海淘MacBook购买攻略；20个美国百货海淘网站与一站式购物指南 |
| 987 | `04_工作与生活 / 生活、出行与消费` | 消费品评测与排行 | 2 | `购物、订阅与生活服务` | `small:consumer-product-reviews` | 盖得排行消费品品牌与产品口碑榜单查询；口碑平替高性价比商品替代品收录与推荐平台 |
| 988 | `04_工作与生活 / 生活、出行与消费` | 消费电子选购 | 1 | `购物、订阅与生活服务` | `small:electronics-shopping` | 一加10 Pro万物青12GB加256GB产品页 |
| 989 | `04_工作与生活 / 生活、出行与消费` | 眼镜与隐形眼镜购物 | 1 | `购物、订阅与生活服务` | `small:eyewear-shopping` | 可得眼镜网上配镜隐形眼镜及美瞳购物商城 |
| 990 | `04_工作与生活 / 生活、出行与消费` | 购物与比价工具 | 1 | `购物、订阅与生活服务` | `small:shopping-tools` | 最佳平替：通过低价替代搜索词寻找高性价比商品 |
| 991 | `04_工作与生活 / 生活、出行与消费` | 购物优惠与折扣 | 2 | `购物、订阅与生活服务` | `small:shopping-deals` | 白菜坑优惠券特价商品与限量抢购信息平台；大忽悠宠粉活动商品商城与 2023 年合集页面 |
| 992 | `04_工作与生活 / 生活、出行与消费` | 软件订阅服务评测 | 1 | `购物、订阅与生活服务` | `small:software-subscriptions` | 爱范儿评测电脑软件订阅服务与使用价值 |
| 993 | `04_工作与生活 / 生活、出行与消费` | 邮政快递服务 | 1 | `购物、订阅与生活服务` | `small:postal-services` | 国家邮政局申诉服务个人投诉查询页面 |
| 994 | `04_工作与生活 / 网站、社区与待整理` | 个人博客 | 2 | `个人站点、博客与社区` | `small:personal-blogs` | 守望轩记录生活思考与个人随笔博客；CVcat 梦想编织屋个人博客文章目录 |
| 995 | `04_工作与生活 / 网站、社区与待整理` | 个人网站主页 | 2 | `个人站点、博客与社区` | `small:personal-websites` | 刘佳珺个人网站与在线主页入口；师爷的康城个人社区与娱乐网站首页 |
| 996 | `04_工作与生活 / 网站、社区与待整理` | 个人页面与社区 | 1 | `个人站点、博客与社区` | `small:personal-communities` | 捡个便宜个人资源分享与交友页面 |
| 997 | `04_工作与生活 / 网站、社区与待整理` | 博客与网站管理 | 1 | `个人站点、博客与社区` | `small:blog-administration` | 内网Halo博客系统管理仪表板 |
| 998 | `04_工作与生活 / 网站、社区与待整理` | 社区与平台 | 1 | `个人站点、博客与社区` | `small:community-platforms` | 河马小镇线上平台与社区服务首页 |
| 999 | `04_工作与生活 / 网站、社区与待整理` | 社群与资源共享 | 2 | `个人站点、博客与社区` | `small:community-resources` | Teambition Thoughts 资源群欢迎与共享页面；Telegram 简体中文热门频道前一百名榜单 |
| 1000 | `04_工作与生活 / 网站、社区与待整理` | 论坛分类信息 | 1 | `个人站点、博客与社区` | `small:forum-classifieds` | 买可乐论坛普通可乐分类版块页面 |
| 1001 | `04_工作与生活 / 网站、社区与待整理` | 逆向与破解技术论坛 | 1 | `个人站点、博客与社区` | `small:reverse-engineering-forums` | 随心逆向破解论坛的软件与游戏技术交流区 |
| 1002 | `04_工作与生活 / 网站、社区与待整理` | 临时号码与短信服务 | 2 | `在线服务与管理入口` | `small:temporary-number-services` | 国内外在线短信接码平台资源合集；无敌接码在线短信接收与临时号码平台 |
| 1003 | `04_工作与生活 / 网站、社区与待整理` | 临时号码与短信服务 | 1 | `在线服务与管理入口` | `small:temporary-phone-services` | SMS-Man 临时虚拟手机号与短信验证码服务 |
| 1004 | `04_工作与生活 / 网站、社区与待整理` | 产品与服务官网 | 1 | `在线服务与管理入口` | `small:product-services` | Airy 官网产品与服务信息页面 |
| 1005 | `04_工作与生活 / 网站、社区与待整理` | 众包与微任务平台 | 2 | `在线服务与管理入口` | `small:microtask-platforms` | Clickworker在线微任务众包兼职赚钱平台；UHRS众包微任务在线标注与任务交易市场入口 |
| 1006 | `04_工作与生活 / 网站、社区与待整理` | 其他服务站点 | 1 | `在线服务与管理入口` | `small:misc-services` | Full Motion网站首页与服务内容入口 |
| 1007 | `04_工作与生活 / 网站、社区与待整理` | 在线平台 | 1 | `在线服务与管理入口` | `small:online-platforms` | WeView 在线网站与内容浏览服务首页 |
| 1008 | `04_工作与生活 / 网站、社区与待整理` | 在线服务控制台 | 2 | `在线服务与管理入口` | `small:service-dashboards` | 闪电服务后台控制台仪表盘；Lunabot AI 平台账户登录与控制台入口 |
| 1009 | `04_工作与生活 / 网站、社区与待整理` | 媒体服务账户 | 1 | `在线服务与管理入口` | `small:media-service-accounts` | Infinite Titan媒体服务器用户中心 |
| 1010 | `04_工作与生活 / 网站、社区与待整理` | 工信人才服务 | 1 | `在线服务与管理入口` | `small:industry-talent-services` | 工业和信息化部人才交流中心官方网站首页 |
| 1011 | `04_工作与生活 / 网站、社区与待整理` | 待确认在线服务 | 2 | `在线服务与管理入口` | `small:uncategorized-services` | Light2F网站服务与功能入口页面；Vposy在线服务网站主页与功能入口 |
| 1012 | `04_工作与生活 / 网站、社区与待整理` | 志愿服务 | 1 | `在线服务与管理入口` | `small:volunteer-service` | 志愿厦门志愿服务用户个人中心 |
| 1013 | `04_工作与生活 / 网站、社区与待整理` | 担保交易服务 | 1 | `在线服务与管理入口` | `small:transaction-services` | 新币担保官网服务入口与平台信息页面 |
| 1014 | `04_工作与生活 / 网站、社区与待整理` | 支付订阅服务 | 1 | `在线服务与管理入口` | `small:payment-services` | 野卡海外软件订阅支付与虚拟卡服务 |
| 1015 | `04_工作与生活 / 网站、社区与待整理` | 本地管理入口 | 1 | `在线服务与管理入口` | `small:local-admin` | ZDH系统主页与本地服务控制入口 |
| 1016 | `04_工作与生活 / 网站、社区与待整理` | 本地管理工具 | 1 | `在线服务与管理入口` | `small:local-admin-tools` | 本地交易记录管理系统的事务列表页面 |
| 1017 | `04_工作与生活 / 网站、社区与待整理` | 票务与抢票服务 | 1 | `在线服务与管理入口` | `small:ticketing-services` | Bypass分流抢票与购票辅助服务平台 |
| 1018 | `04_工作与生活 / 网站、社区与待整理` | 管理后台项目 | 1 | `在线服务与管理入口` | `small:admin-dashboard-projects` | Gin-Vue-Admin前后端分离管理后台开源项目 |
| 1019 | `04_工作与生活 / 网站、社区与待整理` | 网站与网络服务 | 1 | `在线服务与管理入口` | `small:web-services` | 鱼鸢网络网站建设与互联网技术服务官方网站 |
| 1020 | `04_工作与生活 / 网站、社区与待整理` | 网站后台管理 | 2 | `在线服务与管理入口` | `small:website-admin` | 刘佳珺网站后台插件管理控制台；极睿科技CMS后台首页管理平台 |
| 1021 | `04_工作与生活 / 网站、社区与待整理` | 网站管理与备案 | 1 | `在线服务与管理入口` | `small:website-administration` | 工信部ICP域名与IP地址备案信息管理系统 |
| 1022 | `04_工作与生活 / 网站、社区与待整理` | 聊天与社交服务 | 1 | `在线服务与管理入口` | `small:chat-services` | RawChat公益聊天服务与共享站点 |
| 1023 | `04_工作与生活 / 网站、社区与待整理` | 苹果官方服务 | 1 | `在线服务与管理入口` | `small:apple-services` | 苹果中国大陆Apple Store零售店地址列表查询 |
| 1024 | `04_工作与生活 / 网站、社区与待整理` | 虚拟号码与短信服务 | 1 | `在线服务与管理入口` | `small:virtual-number-services` | EOMSG 在线虚拟手机号短信验证码接收平台 |
| 1025 | `04_工作与生活 / 网站、社区与待整理` | 虚拟服务平台 | 2 | `在线服务与管理入口` | `small:virtual-services` | 光速代充自助下单与虚拟服务充值平台；光速代刷自助下单与账号增值服务平台 |
| 1026 | `04_工作与生活 / 网站、社区与待整理` | 证券账户与交易工具 | 2 | `在线服务与管理入口` | `small:securities-account-tools` | 富途牛牛账户登录与美股客户端下载入口；富途牛牛国内账户设置与个人配置页面 |
| 1027 | `04_工作与生活 / 网站、社区与待整理` | 账号与验证服务 | 1 | `在线服务与管理入口` | `small:online-accounts` | 5SIM 在线接收短信验证码与虚拟号码服务 |
| 1028 | `04_工作与生活 / 网站、社区与待整理` | 账户与订阅管理 | 1 | `在线服务与管理入口` | `small:account-management` | 大圣净化星空辽阔账号系统用户管理面板 |
| 1029 | `04_工作与生活 / 网站、社区与待整理` | 链接跳转服务 | 1 | `在线服务与管理入口` | `small:link-services` | vlink 短链接跳转与浏览器打开提示页面 |
| 1030 | `04_工作与生活 / 网站、社区与待整理` | ASU校园账户与工具 | 2 | `待整理与跨主题收藏` | `small:asu-tools` | 亚利桑那州立大学 Zoom 个人账户资料设置；亚利桑那州立大学 Dropbox 免费云盘文件入口 |
| 1031 | `04_工作与生活 / 网站、社区与待整理` | GIF动图与动画 | 1 | `待整理与跨主题收藏` | `small:gif-and-animation` | GIPHY动态GIF动图搜索制作与分享平台 |
| 1032 | `04_工作与生活 / 网站、社区与待整理` | ROM修改工具 | 1 | `待整理与跨主题收藏` | `small:rom-hacking-tools` | Romhacking.net游戏ROM修改工具下载页面 |
| 1033 | `04_工作与生活 / 网站、社区与待整理` | Telegram 使用与资源 | 1 | `待整理与跨主题收藏` | `small:telegram` | Telegram贴纸制作及已有表情包本地下载教程 |
| 1034 | `04_工作与生活 / 网站、社区与待整理` | Web3与元宇宙 | 1 | `待整理与跨主题收藏` | `small:web3-and-metaverse` | 元宇宙系列报告：Web3.0开启互联网新阶段 |
| 1035 | `04_工作与生活 / 网站、社区与待整理` | Web性能测试 | 1 | `待整理与跨主题收藏` | `small:web-performance` | Speedometer 3 浏览器网页应用性能基准测试详情 |
| 1036 | `04_工作与生活 / 网站、社区与待整理` | 专利与知识产权 | 1 | `待整理与跨主题收藏` | `small:patents-intellectual-property` | 世界知识产权组织 WIPO 国际专利与知识产权检索 |
| 1037 | `04_工作与生活 / 网站、社区与待整理` | 专利与知识产权查询 | 1 | `待整理与跨主题收藏` | `small:patent-and-ip` | 中国国家知识产权局专利公布公告查询系统 |
| 1038 | `04_工作与生活 / 网站、社区与待整理` | 东盟国别资料 | 2 | `待整理与跨主题收藏` | `small:asean-country-profiles` | 中国一带一路网文莱国家概况与合作信息页面；中国一带一路网印度尼西亚国家详情与合作动态 |
| 1039 | `04_工作与生活 / 网站、社区与待整理` | 书影音资料整理 | 1 | `待整理与跨主题收藏` | `small:media-catalogs` | 豆瓣未收录书籍影视音乐条目汇总 |
| 1040 | `04_工作与生活 / 网站、社区与待整理` | 互联网工具导航 | 2 | `待整理与跨主题收藏` | `small:tool-directories` | 阿猫阿狗互联网产品运营设计工具导航平台；创造狮设计师与互联网从业者工具资源导航 |
| 1041 | `04_工作与生活 / 网站、社区与待整理` | 交通工具与车型 | 1 | `待整理与跨主题收藏` | `small:vehicle-products` | 张雪机车820RR摩托车产品参数与车型展示 |
| 1042 | `04_工作与生活 / 网站、社区与待整理` | 产业园区项目 | 1 | `待整理与跨主题收藏` | `small:industrial-park-projects` | 鼎祥时代日照基地项目与园区运营信息 |
| 1043 | `04_工作与生活 / 网站、社区与待整理` | 产品参数与性能对比 | 1 | `待整理与跨主题收藏` | `small:product-comparisons` | Versus万物参数规格与性能在线对比平台 |
| 1044 | `04_工作与生活 / 网站、社区与待整理` | 产品反馈与需求管理 | 1 | `待整理与跨主题收藏` | `small:product-feedback` | Canny 用户反馈收集与产品功能投票管理平台 |
| 1045 | `04_工作与生活 / 网站、社区与待整理` | 产品发现与社区 | 2 | `待整理与跨主题收藏` | `small:product-discovery` | Product Hunt 最新科技产品与应用发现社区；一网一匠极速版新产品发现与推荐社区 |
| 1046 | `04_工作与生活 / 网站、社区与待整理` | 产品样机工具 | 1 | `待整理与跨主题收藏` | `small:mockup-tools` | Smartmockups在线产品展示样机快速生成平台 |
| 1047 | `04_工作与生活 / 网站、社区与待整理` | 人物资料与访谈 | 2 | `待整理与跨主题收藏` | `small:people-profiles` | CapRadio电台工作人员Chris Hagan个人简介；摄影师Lucia Litman个人简介与创作背景 |
| 1048 | `04_工作与生活 / 网站、社区与待整理` | 健身训练 | 1 | `待整理与跨主题收藏` | `small:fitness-training` | MuscleWiki按身体部位查询训练动作 |
| 1049 | `04_工作与生活 / 网站、社区与待整理` | 共青团组织服务 | 1 | `待整理与跨主题收藏` | `small:youth-organization` | 网上共青团智慧团建系统组织关系与团员管理平台 |
| 1050 | `04_工作与生活 / 网站、社区与待整理` | 内容编辑与排版 | 1 | `待整理与跨主题收藏` | `small:content-authoring` | 基于 MDX 的微信图文排版在线编辑器 |
| 1051 | `04_工作与生活 / 网站、社区与待整理` | 写作与文本校验 | 1 | `待整理与跨主题收藏` | `small:writing-and-text-tools` | zhlint中文文案格式规范与排版错误校验工具 |
| 1052 | `04_工作与生活 / 网站、社区与待整理` | 创意互动网页工具 | 1 | `待整理与跨主题收藏` | `small:creative-web-tools` | Neave Interactive 创意网页互动与在线小工具合集 |
| 1053 | `04_工作与生活 / 网站、社区与待整理` | 创意服务平台 | 1 | `待整理与跨主题收藏` | `small:creative-marketplaces` | 画加插画师约稿与商业美术交易平台 |
| 1054 | `04_工作与生活 / 网站、社区与待整理` | 创意生成工具 | 1 | `待整理与跨主题收藏` | `small:creative-generators` | RAKKOTOOLS 文本与图片 ASCII 字符画生成器 |
| 1055 | `04_工作与生活 / 网站、社区与待整理` | 剪贴板工具 | 1 | `待整理与跨主题收藏` | `small:clipboard-tools` | 剪切助手提升复制粘贴效率的桌面工具 |
| 1056 | `04_工作与生活 / 网站、社区与待整理` | 加密货币与NFT | 2 | `待整理与跨主题收藏` | `small:crypto-nft` | OpenSea上的CLONE X村上隆潮流头像NFT合集；VestLab加密代币上线与分发信息查询平台 |
| 1057 | `04_工作与生活 / 网站、社区与待整理` | 加密资产交易 | 2 | `待整理与跨主题收藏` | `small:crypto-trading` | 币安加密货币现货与衍生品交易平台；OKX 加密货币交易平台与比特币行情入口 |
| 1058 | `04_工作与生活 / 网站、社区与待整理` | 动物保护公益 | 1 | `待整理与跨主题收藏` | `small:animal-welfare` | 中国小动物保护协会官方网站与公益资讯 |
| 1059 | `04_工作与生活 / 网站、社区与待整理` | 动画与特效生成工具 | 1 | `待整理与跨主题收藏` | `small:animation-generators` | 日式液体特效动画GIF在线生成工具页面 |
| 1060 | `04_工作与生活 / 网站、社区与待整理` | 单位换算工具 | 2 | `待整理与跨主题收藏` | `small:unit-converters` | 转换器网万能在线单位换算与转换工具；转换器网在线长度单位换算工具 |
| 1061 | `04_工作与生活 / 网站、社区与待整理` | 占位内容工具 | 1 | `待整理与跨主题收藏` | `small:placeholder-tools` | CanCMS中文随机假文与页面占位内容生成器 |
| 1062 | `04_工作与生活 / 网站、社区与待整理` | 压缩与归档工具 | 1 | `待整理与跨主题收藏` | `small:archive-tools` | WinRAR Windows 压缩解压软件官方下载与支持页面 |
| 1063 | `04_工作与生活 / 网站、社区与待整理` | 名字查询与取名 | 1 | `待整理与跨主题收藏` | `small:naming-tools` | Behind the Name外国名字含义历史与随机取名网站 |
| 1064 | `04_工作与生活 / 网站、社区与待整理` | 命令行与终端工具 | 2 | `待整理与跨主题收藏` | `small:command-line-tools` | x-cmd 轻量级集成终端命令行工具官网；X-CMD 管理开发工具与扩展命令的脚本框架 |
| 1065 | `04_工作与生活 / 网站、社区与待整理` | 命理与占卜工具 | 1 | `待整理与跨主题收藏` | `small:divination-tools` | Tianfu Agent 紫微斗数八字奇门遁甲命理推演引擎 |
| 1066 | `04_工作与生活 / 网站、社区与待整理` | 图示与流程图工具 | 1 | `待整理与跨主题收藏` | `small:diagram-tools` | 亿图图示 EdrawMax 流程图与图表设计软件 |
| 1067 | `04_工作与生活 / 网站、社区与待整理` | 在线实用工具 | 1 | `待整理与跨主题收藏` | `small:general-online-tools` | OneKeyTools在线实用工具集合与其他工具页面 |
| 1068 | `04_工作与生活 / 网站、社区与待整理` | 在线工具集 | 1 | `待整理与跨主题收藏` | `small:web-tools` | 颜夕网络网站的在线工具集页面 |
| 1069 | `04_工作与生活 / 网站、社区与待整理` | 屏幕录制与演示制作 | 1 | `待整理与跨主题收藏` | `small:screen-recording` | FocuSee自动缩放镜头效果的屏幕录制工具 |
| 1070 | `04_工作与生活 / 网站、社区与待整理` | 展会与招商信息 | 1 | `待整理与跨主题收藏` | `small:exhibitions-events` | E展网国内展会招商信息与会展查询平台 |
| 1071 | `04_工作与生活 / 网站、社区与待整理` | 应用发布工具 | 1 | `待整理与跨主题收藏` | `small:app-publishing` | 摩秀软件应用商店截图与展示图在线生成工具 |
| 1072 | `04_工作与生活 / 网站、社区与待整理` | 录屏与摄像头工具 | 2 | `待整理与跨主题收藏` | `small:screen-recording-and-camera` | oCam电脑屏幕录制软件官方下载页面；iVCam将手机用作电脑摄像头的软件工具 |
| 1073 | `04_工作与生活 / 网站、社区与待整理` | 影像设备评测 | 1 | `待整理与跨主题收藏` | `small:camera-reviews` | DXOMARK相机影像质量测试评分与评测 |
| 1074 | `04_工作与生活 / 网站、社区与待整理` | 待整理项目 | 1 | `待整理与跨主题收藏` | `small:unknown-projects` | Aalab Found实验室或创意项目主页 |
| 1075 | `04_工作与生活 / 网站、社区与待整理` | 待确认技术资源 | 1 | `待整理与跨主题收藏` | `small:uncategorized-tech` | SNPS技术站点CJContent内容页面 |
| 1076 | `04_工作与生活 / 网站、社区与待整理` | 待确认网站 | 1 | `待整理与跨主题收藏` | `small:uncategorized` | tingdong.cn：听懂相关服务与内容网站入口 |
| 1077 | `04_工作与生活 / 网站、社区与待整理` | 待确认网站 | 1 | `待整理与跨主题收藏` | `small:uncategorized-sites` | 01sun匿名网站首页与2025页面入口 |
| 1078 | `04_工作与生活 / 网站、社区与待整理` | 待识别在线应用 | 1 | `待整理与跨主题收藏` | `small:unclassified-web-apps` | VBR 在线应用访问入口 |
| 1079 | `04_工作与生活 / 网站、社区与待整理` | 性能测试工具 | 1 | `待整理与跨主题收藏` | `small:performance-testing` | oha 命令行 HTTP 压力测试与负载生成工具 |
| 1080 | `04_工作与生活 / 网站、社区与待整理` | 技术写作网站项目 | 2 | `待整理与跨主题收藏` | `small:tech-writing-projects` | TechScribes 技术内容网站首页演示页面；TechScribes 技术写作网站项目展示主页 |
| 1081 | `04_工作与生活 / 网站、社区与待整理` | 技术文章与案例 | 1 | `待整理与跨主题收藏` | `small:technology-articles` | Cloudflare账户疑似因异常流量被暂停的经历与警示 |
| 1082 | `04_工作与生活 / 网站、社区与待整理` | 技术社区与组织 | 1 | `待整理与跨主题收藏` | `small:tech-communities` | 亚利桑那州立大学 ASU Hacking Club 社团主页 |
| 1083 | `04_工作与生活 / 网站、社区与待整理` | 技术讨论与社区 | 1 | `待整理与跨主题收藏` | `small:technology-discussions` | Hacker News关于低代码软件开发真实性的讨论 |
| 1084 | `04_工作与生活 / 网站、社区与待整理` | 政治与时事新闻 | 1 | `待整理与跨主题收藏` | `small:politics-news` | 彭博图解习近平崛起与中国权力集中的历程 |
| 1085 | `04_工作与生活 / 网站、社区与待整理` | 文本处理工具 | 1 | `待整理与跨主题收藏` | `small:text-processing` | WordsAway 在线文本处理与文本防检测工具 |
| 1086 | `04_工作与生活 / 网站、社区与待整理` | 新闻媒体 | 1 | `待整理与跨主题收藏` | `small:news-media` | 纽约时报中文网国际新闻与深度报道首页 |
| 1087 | `04_工作与生活 / 网站、社区与待整理` | 新闻媒体机构 | 1 | `待整理与跨主题收藏` | `small:news-and-media` | 新华社机构基本情况与官方信息介绍页面 |
| 1088 | `04_工作与生活 / 网站、社区与待整理` | 新闻评论与观点 | 1 | `待整理与跨主题收藏` | `small:news-commentary` | 凤凰网凤凰评论频道时事观点文章平台 |
| 1089 | `04_工作与生活 / 网站、社区与待整理` | 新闻调查报道 | 1 | `待整理与跨主题收藏` | `small:news-investigations` | BBC中文调查性侵影片网站幕后主谋报道 |
| 1090 | `04_工作与生活 / 网站、社区与待整理` | 日剧指南 | 1 | `待整理与跨主题收藏` | `small:japanese-drama-guides` | 91日剧寻找好看日剧与追新剧指南 |
| 1091 | `04_工作与生活 / 网站、社区与待整理` | 日常实用工具 | 1 | `待整理与跨主题收藏` | `small:daily-tools` | 手机在线亲戚称呼关系计算器工具 |
| 1092 | `04_工作与生活 / 网站、社区与待整理` | 日常实用工具 | 1 | `待整理与跨主题收藏` | `small:utility-tools` | 蛙蛙工具在线人民币金额大写转换器 |
| 1093 | `04_工作与生活 / 网站、社区与待整理` | 时间与时区工具 | 1 | `待整理与跨主题收藏` | `small:time-and-timezone-tools` | Every Time Zone全球时差换算与会议时间工具 |
| 1094 | `04_工作与生活 / 网站、社区与待整理` | 智能驾驶产业资讯 | 1 | `待整理与跨主题收藏` | `small:autonomous-driving-news` | 飞凡智驾首席科学家离职与上汽业务整合报道 |
| 1095 | `04_工作与生活 / 网站、社区与待整理` | 本科教学质量报告 | 2 | `待整理与跨主题收藏` | `small:teaching-quality-reports` | 金陵科技学院2022至2023学年本科教学质量报告；广州航海学院2022至2023年度本科教学质量报告 |
| 1096 | `04_工作与生活 / 网站、社区与待整理` | 极客设备与桌搭 | 1 | `待整理与跨主题收藏` | `small:tech-setups` | Geekerwan RoomTour 极客玩家房间设备展示 |
| 1097 | `04_工作与生活 / 网站、社区与待整理` | 桌面美化工具 | 2 | `待整理与跨主题收藏` | `small:desktop-customization` | Rainmeter Windows桌面美化与系统监控组件工具；米哈游人工桌面鹿鸣动态壁纸官方页面 |
| 1098 | `04_工作与生活 / 网站、社区与待整理` | 模拟工具 | 1 | `待整理与跨主题收藏` | `small:simulation-tools` | NUKEMAP核爆炸影响范围在线模拟地图工具 |
| 1099 | `04_工作与生活 / 网站、社区与待整理` | 正则表达式工具与规则 | 2 | `待整理与跨主题收藏` | `small:regex-tools` | Any-Rule 常用正则表达式规则大全；常用正则表达式在线生成与测试工具 |
| 1100 | `04_工作与生活 / 网站、社区与待整理` | 活动创意与案例灵感 | 1 | `待整理与跨主题收藏` | `small:creative-inspiration` | 黑猫会网易主题活动创意案例图片搜索结果 |
| 1101 | `04_工作与生活 / 网站、社区与待整理` | 消息平台与机器人 | 1 | `待整理与跨主题收藏` | `small:messaging-and-bots` | Telegram 机器人目录与评分排行平台 |
| 1102 | `04_工作与生活 / 网站、社区与待整理` | 演讲与思想分享 | 1 | `待整理与跨主题收藏` | `small:talks-ideas` | TED 全球思想演讲与知识传播平台 |
| 1103 | `04_工作与生活 / 网站、社区与待整理` | 演讲与沟通表达 | 1 | `待整理与跨主题收藏` | `small:communication-skills` | Gary Yang 杨天颖演讲表达与商务呈现教练资源 |
| 1104 | `04_工作与生活 / 网站、社区与待整理` | 烟草产品信息 | 1 | `待整理与跨主题收藏` | `small:tobacco-information` | 烟悦网利群香烟产品资料与信息搜索结果 |
| 1105 | `04_工作与生活 / 网站、社区与待整理` | 热点榜单聚合 | 1 | `待整理与跨主题收藏` | `small:news-aggregators` | 今日热榜聚合各平台实时热点榜单 |
| 1106 | `04_工作与生活 / 网站、社区与待整理` | 爱死亡和机器人 | 2 | `待整理与跨主题收藏` | `small:love-death-robots` | 爱死亡和机器人第二季字幕组资源页；爱死亡和机器人动画剧集视频页面 |
| 1107 | `04_工作与生活 / 网站、社区与待整理` | 犬种与犬展服务 | 2 | `待整理与跨主题收藏` | `small:dog-breeds-and-shows` | 中国犬业协会 CKU 官方犬种赛事服务网站；CKU 犬种百科中的萨摩耶犬品种资料 |
| 1108 | `04_工作与生活 / 网站、社区与待整理` | 犬舍与繁育信息 | 1 | `待整理与跨主题收藏` | `small:dog-breeding` | 摩王家族萨摩耶犬舍繁育犬只信息页面 |
| 1109 | `04_工作与生活 / 网站、社区与待整理` | 生态保护项目 | 1 | `待整理与跨主题收藏` | `small:conservation-projects` | 全球一百种失落物种保护专题项目 |
| 1110 | `04_工作与生活 / 网站、社区与待整理` | 用户认证与权限 | 1 | `待整理与跨主题收藏` | `small:authentication` | SuperTokens 开源用户认证与会话管理方案 |
| 1111 | `04_工作与生活 / 网站、社区与待整理` | 电动车资讯与报价 | 1 | `待整理与跨主题收藏` | `small:electric-vehicles` | 全球电动车网电动车与电动汽车报价资讯平台 |
| 1112 | `04_工作与生活 / 网站、社区与待整理` | 电子制造与PCB | 1 | `待整理与跨主题收藏` | `small:electronics-manufacturing` | 嘉立创国产PCB线路板CAM软件产品官网 |
| 1113 | `04_工作与生活 / 网站、社区与待整理` | 界面模拟工具 | 1 | `待整理与跨主题收藏` | `small:ui-mockups` | 在线健康码界面模拟工具 |
| 1114 | `04_工作与生活 / 网站、社区与待整理` | 短信测试工具 | 2 | `待整理与跨主题收藏` | `small:sms-testing` | 蓝奏云分享的短信压力测试软件安装包；在线短信压力测试服务网站入口页面 |
| 1115 | `04_工作与生活 / 网站、社区与待整理` | 禅修与心灵文字 | 1 | `待整理与跨主题收藏` | `small:meditation` | 内酷禅心关于清静寂定心境的文字站 |
| 1116 | `04_工作与生活 / 网站、社区与待整理` | 移动应用与安装包 | 1 | `待整理与跨主题收藏` | `small:mobile-apps` | 抖音iOS应用30.3.0版IPA安装包下载 |
| 1117 | `04_工作与生活 / 网站、社区与待整理` | 移动设备管理工具 | 2 | `待整理与跨主题收藏` | `small:mobile-device-tools` | 爱思助手苹果设备刷机越狱与管理工具官网；CzDEV搞机工具箱第三方文件分享下载页面 |
| 1118 | `04_工作与生活 / 网站、社区与待整理` | 简历写作指南 | 1 | `待整理与跨主题收藏` | `small:resume-guides` | 简单简历基础内容模块填写与撰写指南 |
| 1119 | `04_工作与生活 / 网站、社区与待整理` | 算法与刷题学习 | 2 | `待整理与跨主题收藏` | `small:algorithm-learning` | 代码随想录算法刷题路线与数据结构教程；LeetCode 动态规划刷题 Notion 学习模板 |
| 1120 | `04_工作与生活 / 网站、社区与待整理` | 红色警戒工具 | 2 | `待整理与跨主题收藏` | `small:red-alert-tools` | 我要玩游戏网站与红警修改工具入口；我要玩游戏红色警戒修改大师入口 |
| 1121 | `04_工作与生活 / 网站、社区与待整理` | 绝命毒师 | 2 | `待整理与跨主题收藏` | `small:breaking-bad` | 绝命毒师第五季第九集在线播放页面；绝命毒师第三季第一集剧迷播放页 |
| 1122 | `04_工作与生活 / 网站、社区与待整理` | 综合在线工具 | 1 | `待整理与跨主题收藏` | `small:online-utilities` | 随便吧综合在线生成查询与网名设计工具集 |
| 1123 | `04_工作与生活 / 网站、社区与待整理` | 综合新闻资讯 | 2 | `待整理与跨主题收藏` | `small:general-news` | 界面新闻重点时政财经商业新闻平台；凤凰网综合新闻资讯与媒体门户网站 |
| 1124 | `04_工作与生活 / 网站、社区与待整理` | 网站导航目录 | 1 | `待整理与跨主题收藏` | `small:web-directories` | a667a 网站导航分类页面 |
| 1125 | `04_工作与生活 / 网站、社区与待整理` | 网站性能测试 | 1 | `待整理与跨主题收藏` | `small:web-performance-tools` | WebPageTest 对 xwww.online 的网站性能测试结果 |
| 1126 | `04_工作与生活 / 网站、社区与待整理` | 网站模板资源 | 1 | `待整理与跨主题收藏` | `small:website-templates` | Envato Elements 的 Elementor 网站模板套件资源库 |
| 1127 | `04_工作与生活 / 网站、社区与待整理` | 网络与通用工具 | 1 | `待整理与跨主题收藏` | `small:web-utilities` | DuckDuckGo注重隐私保护的互联网搜索引擎首页 |
| 1128 | `04_工作与生活 / 网站、社区与待整理` | 网络直播 | 1 | `待整理与跨主题收藏` | `small:live-tv` | 直播大全在线直播频道地区分类页 |
| 1129 | `04_工作与生活 / 网站、社区与待整理` | 网页WebGL特效 | 1 | `待整理与跨主题收藏` | `small:webgl-web-effects` | Codrops Infinite Lights炫酷流光网页特效演示 |
| 1130 | `04_工作与生活 / 网站、社区与待整理` | 脑力训练与测试 | 2 | `待整理与跨主题收藏` | `small:cognitive-training` | Lumosity认知能力训练与益智游戏平台；Human Benchmark 在线认知能力与反应速度测试平台 |
| 1131 | `04_工作与生活 / 网站、社区与待整理` | 自托管应用 | 1 | `待整理与跨主题收藏` | `small:self-hosted-apps` | Immich自托管照片与视频管理项目官网 |
| 1132 | `04_工作与生活 / 网站、社区与待整理` | 航班追踪查询 | 2 | `待整理与跨主题收藏` | `small:flight-tracking` | 厦门航空MF8593航班2023年3月历史飞行记录；FlightAware实时航班状态与飞行轨迹查询 |
| 1133 | `04_工作与生活 / 网站、社区与待整理` | 航空与航班追踪 | 1 | `待整理与跨主题收藏` | `small:aviation-tracking` | FlightAware航班追踪服务账户管理页面 |
| 1134 | `04_工作与生活 / 网站、社区与待整理` | 英雄联盟手游辅助 | 1 | `待整理与跨主题收藏` | `small:lol-mobile-assistance` | 壬申工作室提供的英雄联盟手游外挂网站 |
| 1135 | `04_工作与生活 / 网站、社区与待整理` | 药品监管查询 | 2 | `待整理与跨主题收藏` | `small:drug-regulation` | 国家药品监督管理局药品医疗器械数据查询；国家药监局化妆品备案注册数据查询入口 |
| 1136 | `04_工作与生活 / 网站、社区与待整理` | 虚拟主播动态 | 1 | `待整理与跨主题收藏` | `small:virtual-streamers` | VTBs in bilibili虚拟主播动态聚合 |
| 1137 | `04_工作与生活 / 网站、社区与待整理` | 表单与问卷工具 | 1 | `待整理与跨主题收藏` | `small:form-tools` | TDUCK 填鸭表单：在线表单收集与数据管理平台 |
| 1138 | `04_工作与生活 / 网站、社区与待整理` | 观赏鱼饲养 | 2 | `待整理与跨主题收藏` | `small:ornamental-fish` | 龙鱼之巅关于粗线银版龙鱼的讨论帖子；地图鱼品种介绍与观赏鱼百科资料 |
| 1139 | `04_工作与生活 / 网站、社区与待整理` | 设备互联工具 | 1 | `待整理与跨主题收藏` | `small:device-connectivity` | OPPO一加设备跨屏互联官方下载入口 |
| 1140 | `04_工作与生活 / 网站、社区与待整理` | 设备管理与工具 | 2 | `待整理与跨主题收藏` | `small:device-tools` | Karl's Drive中的联想设备相关工具正式版资源；联想LegionZone游戏设备官方服务与管理平台首页 |
| 1141 | `04_工作与生活 / 网站、社区与待整理` | 设备维修与拆解指南 | 1 | `待整理与跨主题收藏` | `small:repair-guides` | iFixit中文站：免费电子设备维修拆解手册 |
| 1142 | `04_工作与生活 / 网站、社区与待整理` | 跑步与运动记录 | 1 | `待整理与跨主题收藏` | `small:fitness-and-running` | Running Page个人跑步记录与运动数据主页 |
| 1143 | `04_工作与生活 / 网站、社区与待整理` | 路由器评测 | 1 | `待整理与跨主题收藏` | `small:router-reviews` | 中国移动RAX3000Q路由器拆机评测交流 |
| 1144 | `04_工作与生活 / 网站、社区与待整理` | 车辆信息查询 | 1 | `待整理与跨主题收藏` | `small:vehicle-lookups` | 驾照网全国车辆车牌号归属地在线查询 |
| 1145 | `04_工作与生活 / 网站、社区与待整理` | 车辆公告与法规 | 1 | `待整理与跨主题收藏` | `small:vehicle-regulations` | 工信部第333批道路机动车生产企业及产品公告变更名单 |
| 1146 | `04_工作与生活 / 网站、社区与待整理` | 软件商城与账户 | 2 | `待整理与跨主题收藏` | `small:software-stores` | 数码荔枝软件商店个人账号设置管理页面；数码荔枝正版软件商城与优秀应用推荐首页 |
| 1147 | `04_工作与生活 / 网站、社区与待整理` | 软件安装与使用 | 1 | `待整理与跨主题收藏` | `small:software-installation` | TORCS 赛车模拟器源代码安装与运行说明 |
| 1148 | `04_工作与生活 / 网站、社区与待整理` | 软件密钥与激活资源 | 1 | `待整理与跨主题收藏` | `small:software-keys` | 爱好网共享密钥资源与激活信息发布页面 |
| 1149 | `04_工作与生活 / 网站、社区与待整理` | 软件架构 | 1 | `待整理与跨主题收藏` | `small:software-architecture` | 微服务架构设计模式视频课程购买与学习页面 |
| 1150 | `04_工作与生活 / 网站、社区与待整理` | 软件资源社区 | 1 | `待整理与跨主题收藏` | `small:software-communities` | Mobilism移动应用资源与论坛社区首页 |
| 1151 | `04_工作与生活 / 网站、社区与待整理` | 软件项目与扩展 | 2 | `待整理与跨主题收藏` | `small:software-projects` | Microblock 软件插件与开源项目资源聚合主页；Bitbucket开源项目letsgogo_21代码仓库说明文档 |
| 1152 | `04_工作与生活 / 网站、社区与待整理` | 追星与活动记录 | 1 | `待整理与跨主题收藏` | `small:fandom-and-events` | 推活日记：记录偶像应援活动与线下演出行程 |
| 1153 | `04_工作与生活 / 网站、社区与待整理` | 通信与号码工具 | 1 | `待整理与跨主题收藏` | `small:telecom-tools` | FreeCarrierLookup美国手机号码运营商免费查询工具 |
| 1154 | `04_工作与生活 / 网站、社区与待整理` | 通用实用工具 | 1 | `待整理与跨主题收藏` | `small:general-utility-tools` | CC趣味屋在线二维码生成与二维码解码工具 |
| 1155 | `04_工作与生活 / 网站、社区与待整理` | 通用查询工具 | 1 | `待整理与跨主题收藏` | `small:general-query-tools` | IP138地址归属地与号码信息在线查询 |
| 1156 | `04_工作与生活 / 网站、社区与待整理` | 通知与消息推送 | 1 | `待整理与跨主题收藏` | `small:notification-tools` | WePush多渠道消息批量推送工具与配置说明 |
| 1157 | `04_工作与生活 / 网站、社区与待整理` | 配置管理与中间件 | 1 | `待整理与跨主题收藏` | `small:configuration-management` | Apollo分布式配置中心项目中文使用文档首页 |
| 1158 | `04_工作与生活 / 网站、社区与待整理` | 链接检测工具 | 1 | `待整理与跨主题收藏` | `small:link-tools` | WhatsLink陌生网址跳转目标安全查询工具 |
| 1159 | `04_工作与生活 / 网站、社区与待整理` | 零售与即时配送 | 1 | `待整理与跨主题收藏` | `small:retail-delivery` | 同城酒库酒类零售与即时配送服务官网 |
| 1160 | `04_工作与生活 / 网站、社区与待整理` | 静态站点与博客工具 | 1 | `待整理与跨主题收藏` | `small:static-site-tools` | Hugo 高性能静态网站生成器官方网站 |
| 1161 | `04_工作与生活 / 网站、社区与待整理` | 风险工具归档 | 1 | `待整理与跨主题收藏` | `small:risky-tools` | 在线短信轰炸工具及骚扰服务页面 |
| 1162 | `04_工作与生活 / 网站、社区与待整理` | 高性能计算与EDA | 2 | `待整理与跨主题收藏` | `small:hpc-and-eda` | 速石科技Fsched与SLF集群调度器介绍；速石科技EDA仿真与云计算案例资料中心 |
| 1163 | `04_工作与生活 / 网站、社区与待整理` | 高性能计算与集群 | 1 | `待整理与跨主题收藏` | `small:hpc-and-clusters` | Slurm高性能计算任务调度器官方文档 |
| 1164 | `04_工作与生活 / 网站、社区与待整理` | 高端珠宝产品 | 1 | `待整理与跨主题收藏` | `small:luxury-jewelry` | 海瑞温斯顿Emerald系列18K白金珠宝产品页 |
| 1165 | `04_工作与生活 / 网站、社区与待整理` | 高级腕表 | 1 | `待整理与跨主题收藏` | `small:luxury-watches` | 江诗丹顿官方全系列高级腕表选购页面 |
| 1166 | `04_工作与生活 / 网站、社区与待整理` | 高风险与滥用内容 | 1 | `待整理与跨主题收藏` | `small:unsafe-content` | 在线短信轰炸服务页面 |
| 1167 | `04_工作与生活 / 网站、社区与待整理` | 鬼谷八荒修改工具 | 2 | `待整理与跨主题收藏` | `small:guigubahuang-tools` | 鬼谷八荒主宰修改器2.3.1下载页面；鬼谷八荒ToGameMod内置修改器更新讨论帖 |
| 1168 | `04_工作与生活 / 网站、社区与待整理` | 成人社区讨论 | 1 | `成人内容与敏感站点` | `small:adult-communities` | 141华人社区香港成人话题讨论专区 |
| 1169 | `04_工作与生活 / 网站、社区与待整理` | 成人网站推荐与评测 | 2 | `成人内容与敏感站点` | `small:adult-site-guides` | Porn Dude成人陪侍网站推荐目录；Porn Dude中文色情交友网站推荐与评测 |
| 1170 | `04_工作与生活 / 网站、社区与待整理` | 成人话题讨论 | 1 | `成人内容与敏感站点` | `small:adult-discussions` | PTT网友分享一天内多次射精的个人经验讨论 |
| 1171 | `04_工作与生活 / 网站、社区与待整理` | Telegram 资源与工具 | 1 | `网站导航与资源入口` | `small:telegram-resources` | Telegram机器人资源整理与常见功能介绍文章 |
| 1172 | `04_工作与生活 / 网站、社区与待整理` | 免费资源导航 | 1 | `网站导航与资源入口` | `small:free-resources` | FMHY免费媒体软件与网络资源导航索引 |
| 1173 | `04_工作与生活 / 网站、社区与待整理` | 管理后台与登录 | 1 | `网站导航与资源入口` | `small:admin-portals` | SherryYun网站后台管理系统登录入口 |
| 1174 | `04_工作与生活 / 网站、社区与待整理` | 网址导航 | 1 | `网站导航与资源入口` | `small:site-navigation` | I社中国相关网址发布导航页面 |
| 1175 | `04_工作与生活 / 网站、社区与待整理` | 美剧资源 | 2 | `网站导航与资源入口` | `small:american-series-resources` | 美剧虫蓝光美剧影视资源聚合网站；爱美剧美剧在线观看与下载资源网站 |
| 1176 | `04_工作与生活 / 网站、社区与待整理` | 苹果软件资源 | 1 | `网站导航与资源入口` | `small:apple-software-resources` | 沧海苹果应用站热门苹果软件资源平台首页 |
| 1177 | `04_工作与生活 / 网站、社区与待整理` | 账户与平台入口 | 1 | `网站导航与资源入口` | `small:account-portals` | Outlier Community 账户偏好设置页面 |
| 1178 | `04_工作与生活 / 职业、政务与金融` | 兼职与招聘信息 | 1 | `招聘、求职与职业发展` | `small:job-listings` | Fiwfans泰语兼职服务信息聚合平台 |
| 1179 | `04_工作与生活 / 职业、政务与金融` | 创意与技术职位 | 1 | `招聘、求职与职业发展` | `small:creative-job-boards` | Authentic Jobs设计创意与开发职位招聘板 |
| 1180 | `04_工作与生活 / 职业、政务与金融` | 实习与职位招聘 | 2 | `招聘、求职与职业发展` | `small:internships-and-jobs` | 厦门人才网本地招聘求职与人力资源服务平台；实习僧全国实习岗位搜索与招聘服务平台 |
| 1181 | `04_工作与生活 / 职业、政务与金融` | 招聘与求职 | 1 | `招聘、求职与职业发展` | `small:jobs` | Gaggle Net公司公开招聘职位与求职申请页面 |
| 1182 | `04_工作与生活 / 职业、政务与金融` | 招聘管理工具 | 1 | `招聘、求职与职业发展` | `small:recruitment-tools` | 智联招聘企业用户快速认证与账号开通页面 |
| 1183 | `04_工作与生活 / 职业、政务与金融` | 求职与技术面试 | 1 | `招聘、求职与职业发展` | `small:career-and-interviews` | 技术面试八股文知识点与求职备考资料页面 |
| 1184 | `04_工作与生活 / 职业、政务与金融` | 求职与职业工具 | 1 | `招聘、求职与职业发展` | `small:career-tools` | 老鱼简历在线创建编辑与展示个人求职简历页面 |
| 1185 | `04_工作与生活 / 职业、政务与金融` | 求职与职业机会 | 1 | `招聘、求职与职业发展` | `small:jobs-and-careers` | 华盛顿特区远程转录专员职位申请页面 |
| 1186 | `04_工作与生活 / 职业、政务与金融` | 求职经验与职业发展 | 1 | `招聘、求职与职业发展` | `small:career-guides` | 李辉分享五年待业后的求职经历文章 |
| 1187 | `04_工作与生活 / 职业、政务与金融` | 求职面试准备 | 1 | `招聘、求职与职业发展` | `small:job-interview` | 行为事件访谈与胜任力面试自我介绍准备指南 |
| 1188 | `04_工作与生活 / 职业、政务与金融` | 海外职业与移民 | 2 | `招聘、求职与职业发展` | `small:overseas-career` | 德国技术人才移民法与工作居留政策说明；TechPays Europe 欧洲科技行业程序员薪资查询平台 |
| 1189 | `04_工作与生活 / 职业、政务与金融` | 科技职业与薪资 | 2 | `招聘、求职与职业发展` | `small:tech-careers` | 2022年全球各国程序员薪资水平与收入差距报告；2022年各国软件工程师平均薪资数据比较报告 |
| 1190 | `04_工作与生活 / 职业、政务与金融` | 综合求职与招聘平台 | 2 | `招聘、求职与职业发展` | `small:general-job-boards` | Monster综合职位搜索职业建议与招聘资源平台；CareerBuilder全球地区招聘站点入口页面 |
| 1191 | `04_工作与生活 / 职业、政务与金融` | 职业与行业观察 | 1 | `招聘、求职与职业发展` | `small:career-and-industry` | 金融行业程序员工作职责与职业体验访谈文章 |
| 1192 | `04_工作与生活 / 职业、政务与金融` | 金融行业求职 | 2 | `招聘、求职与职业发展` | `small:finance-careers` | AlphaSights中文客户服务专员2024招聘职位；职业梦垂直金融行业招聘与职业培训平台 |
| 1193 | `04_工作与生活 / 职业、政务与金融` | 零售行业求职 | 1 | `招聘、求职与职业发展` | `small:retail-careers` | 优衣库中国招聘网申系统与职位申请入口 |
| 1194 | `04_工作与生活 / 职业、政务与金融` | 个人金融服务 | 1 | `政务、公共服务与政策` | `small:personal-finance-services` | 中国人民银行征信中心个人信用查询 |
| 1195 | `04_工作与生活 / 职业、政务与金融` | 人社政务服务 | 2 | `政务、公共服务与政策` | `small:human-resources-services` | 国家12333人力资源和社会保障在线服务目录；江苏领航人力资源服务集团官方网站 |
| 1196 | `04_工作与生活 / 职业、政务与金融` | 企业政务服务 | 2 | `政务、公共服务与政策` | `small:business-government` | 浙江省企业登记全程电子化在线办理平台；杭州亲清在线政企服务与惠企政策平台 |
| 1197 | `04_工作与生活 / 职业、政务与金融` | 住房保障服务 | 1 | `政务、公共服务与政策` | `small:housing-services` | 深圳人才安居住房服务官方网站 |
| 1198 | `04_工作与生活 / 职业、政务与金融` | 公共服务 | 2 | `政务、公共服务与政策` | `small:public-services` | 通信大数据行程卡官方登录与行程查询服务入口；互联网信息服务投诉平台我的投诉查询页 |
| 1199 | `04_工作与生活 / 职业、政务与金融` | 厦门人才政策 | 1 | `政务、公共服务与政策` | `small:xiamen-talent-policy` | 厦门市博士人才落户政策公共服务说明 |
| 1200 | `04_工作与生活 / 职业、政务与金融` | 广东政务服务 | 1 | `政务、公共服务与政策` | `small:guangdong-public-services` | 广东省人力资源和社会保障厅网上服务平台 |
| 1201 | `04_工作与生活 / 职业、政务与金融` | 政务与公共服务 | 2 | `政务、公共服务与政策` | `small:government-public-services` | 黑龙江省中医药相关官方网站首页；国家技能人才评价证书全国联网查询系统 |
| 1202 | `04_工作与生活 / 职业、政务与金融` | 政府政策与规划 | 1 | `政务、公共服务与政策` | `small:government-policy` | 厦门市十四五生态文明建设规划实施通知 |
| 1203 | `04_工作与生活 / 职业、政务与金融` | 政府采购服务 | 1 | `政务、公共服务与政策` | `small:government-procurement` | 江西省政府采购电子卖场供应商管理入口 |
| 1204 | `04_工作与生活 / 职业、政务与金融` | 标准与政策 | 2 | `政务、公共服务与政策` | `small:standards-and-policy` | 全国标准信息公共服务平台标准检索首页；GB/T 36073-2018 数据管理能力成熟度评估模型 |
| 1205 | `04_工作与生活 / 职业、政务与金融` | 海关政务服务 | 1 | `政务、公共服务与政策` | `small:customs-government` | 中华人民共和国海关总署政务服务与海关政策信息官网 |
| 1206 | `04_工作与生活 / 职业、政务与金融` | 深圳公共服务 | 1 | `政务、公共服务与政策` | `small:shenzhen-public-services` | 深圳金融社保卡申领办理指南与更新信息 |
| 1207 | `04_工作与生活 / 职业、政务与金融` | 自由职业与外包服务 | 2 | `政务、公共服务与政策` | `small:freelance-services` | Fiverr全球自由职业音视频定制服务交易平台；猪八戒网企业服务与自由职业项目交易平台 |
| 1208 | `04_工作与生活 / 职业、政务与金融` | 金融账户与支付 | 2 | `政务、公共服务与政策` | `small:financial-services` | Fiat24数字金融账户个人仪表盘；PayPal美国在线支付转账与资金管理首页 |
| 1209 | `04_工作与生活 / 职业、政务与金融` | 人才政策服务 | 1 | `职业与社会服务资源` | `small:talent-policies` | 杭州市人才政策详情与申报服务页面 |
| 1210 | `04_工作与生活 / 职业、政务与金融` | 厦门落户政策 | 1 | `职业与社会服务资源` | `small:xiamen-residency` | 厦门人才引进与落户政策办理信息 |
| 1211 | `04_工作与生活 / 职业、政务与金融` | 国际职业技能赛事 | 1 | `职业与社会服务资源` | `small:international-skills-competitions` | 一带一路暨金砖国家技能发展技术创新大赛官网 |
| 1212 | `04_工作与生活 / 职业、政务与金融` | 投资与基金 | 1 | `职业与社会服务资源` | `small:investing` | 丁忆童Rookie Fund基金会员服务页面 |
| 1213 | `04_工作与生活 / 职业、政务与金融` | 投资交流社区 | 1 | `职业与社会服务资源` | `small:investing-communities` | 雪球中国股票投资者交流社区与行情资讯平台 |
| 1214 | `04_工作与生活 / 职业、政务与金融` | 深圳落户政策 | 2 | `职业与社会服务资源` | `small:shenzhen-residency` | 深圳应届生落户是否需要报到证的解答；深圳就业报到证办理所需材料说明 |
| 1215 | `04_工作与生活 / 职业、政务与金融` | 科技与投资观点 | 1 | `职业与社会服务资源` | `small:technology-and-investing` | 硅谷王川的科技投资观点与个人博客主页 |
| 1216 | `04_工作与生活 / 职业、政务与金融` | 职业档案与人脉 | 1 | `职业与社会服务资源` | `small:professional-profiles` | 李业章个人LinkedIn职业档案主页 |
| 1217 | `04_工作与生活 / 职业、政务与金融` | 自由职业与服务平台 | 1 | `职业与社会服务资源` | `small:freelance-work` | Fiverr卖家服务项目编辑与发布管理页面 |
| 1218 | `04_工作与生活 / 职业、政务与金融` | 自由职业平台 | 2 | `职业与社会服务资源` | `small:freelance-platforms` | Freelancer自由职业平台帮助支持分类中心；Upwork全球自由职业者与远程项目招聘平台 |
| 1219 | `04_工作与生活 / 职业、政务与金融` | 量化投资 | 1 | `职业与社会服务资源` | `small:quantitative-investing` | BigQuant量化投资研究方法与平台功能说明 |
| 1220 | `04_工作与生活 / 职业、政务与金融` | 金融投资工具 | 1 | `职业与社会服务资源` | `small:financial-tools` | FinceptTerminal：面向专业人士的金融情报终端 |
| 1221 | `04_工作与生活 / 职业、政务与金融` | 金融监管 | 1 | `职业与社会服务资源` | `small:financial-regulation` | 国家金融监督管理总局官方网站与金融监管信息 |
| 1222 | `04_工作与生活 / 职业、政务与金融` | 金融计算工具 | 1 | `职业与社会服务资源` | `small:financial-calculators` | 在线利息计算器：计算本金、利率与收益 |
| 1223 | `04_工作与生活 / 职业、政务与金融` | 高端人才外包招聘 | 1 | `职业与社会服务资源` | `small:talent-outsourcing` | Toptal 全球高端自由职业人才雇佣平台 |
| 1224 | `04_工作与生活 / 职业、政务与金融` | 个人征信 | 1 | `金融、投资、税务与信用` | `small:personal-credit` | 中国人民银行个人信用信息查询服务平台 |
| 1225 | `04_工作与生活 / 职业、政务与金融` | 个税与薪资计算 | 1 | `金融、投资、税务与信用` | `small:tax-and-payroll` | 年终奖个税反算与所得税计算器（2020） |
| 1226 | `04_工作与生活 / 职业、政务与金融` | 企业银行与财务 | 1 | `金融、投资、税务与信用` | `small:business-banking` | Mercury 面向初创企业与小企业的在线商业银行服务 |
| 1227 | `04_工作与生活 / 职业、政务与金融` | 保险业务平台 | 1 | `金融、投资、税务与信用` | `small:insurance-business` | 中国人寿国寿e店业务平台扫码登录入口 |
| 1228 | `04_工作与生活 / 职业、政务与金融` | 保险行业信息 | 1 | `金融、投资、税务与信用` | `small:insurance-industry` | 中国保险行业协会官方网站与行业资讯服务 |
| 1229 | `04_工作与生活 / 职业、政务与金融` | 创业融资与并购 | 1 | `金融、投资、税务与信用` | `small:startup-finance` | 方创资本创业融资与企业并购财务顾问服务 |
| 1230 | `04_工作与生活 / 职业、政务与金融` | 医疗保险服务 | 2 | `金融、投资、税务与信用` | `small:health-insurance` | 爱牙保障计划指定口腔保险门诊查询页面；2023年度上海沪惠保投保入口与常见问题解答 |
| 1231 | `04_工作与生活 / 职业、政务与金融` | 地方招商与园区 | 1 | `金融、投资、税务与信用` | `small:local-investment-promotion` | 鄂州招商网产业扶持政策与工业园区招商信息 |
| 1232 | `04_工作与生活 / 职业、政务与金融` | 投资交易平台 | 1 | `金融、投资、税务与信用` | `small:investment-platforms` | RockFlow智能交易平台开户链接与投资服务主页 |
| 1233 | `04_工作与生活 / 职业、政务与金融` | 投资机构与资管公司 | 1 | `金融、投资、税务与信用` | `small:investment-firms` | GROW Investment Group投资集团官方网站 |
| 1234 | `04_工作与生活 / 职业、政务与金融` | 投资知识学习 | 1 | `金融、投资、税务与信用` | `small:investment-education` | Investopedia金融投资知识学习与术语解释平台 |
| 1235 | `04_工作与生活 / 职业、政务与金融` | 支付与金融服务 | 2 | `金融、投资、税务与信用` | `small:payments-and-finance` | BigQuant 人工智能量化投资研究与交易平台；PayPal 商家工具与收款账户偏好设置页面 |
| 1236 | `04_工作与生活 / 职业、政务与金融` | 支付与金融账户 | 1 | `金融、投资、税务与信用` | `small:payments-finance` | 支付宝账户登录与个人支付服务入口 |
| 1237 | `04_工作与生活 / 职业、政务与金融` | 新加坡银行服务 | 1 | `金融、投资、税务与信用` | `small:singapore-banking` | 华侨银行OCBC网上银行登录与账户管理入口 |
| 1238 | `04_工作与生活 / 职业、政务与金融` | 汽车金融计算 | 2 | `金融、投资、税务与信用` | `small:car-finance` | 太平洋汽车购车贷款分期付款计算器页面；特斯拉中国汽车金融贷款月供计算服务页面 |
| 1239 | `04_工作与生活 / 职业、政务与金融` | 网上银行服务 | 2 | `金融、投资、税务与信用` | `small:online-banking` | 中国银行个人网上银行登录服务页面；中国银行iGTB企业网上银行业务入口 |
| 1240 | `04_工作与生活 / 职业、政务与金融` | 美国企业与税务 | 1 | `金融、投资、税务与信用` | `small:us-business-tax` | 美国企业在线申请EIN税号流程指南 |
| 1241 | `04_工作与生活 / 职业、政务与金融` | 美国信用卡 | 1 | `金融、投资、税务与信用` | `small:us-credit-cards` | 美国信用卡新手申请入门与首卡选择推荐指南 |
| 1242 | `04_工作与生活 / 职业、政务与金融` | 财税记账服务 | 1 | `金融、投资、税务与信用` | `small:accounting-tax` | 自记账零基础企业记账与报税服务 |
| 1243 | `04_工作与生活 / 职业、政务与金融` | 跨境银行与加密游牧 | 2 | `金融、投资、税务与信用` | `small:cross-border-banking` | 加密游牧指南：在格鲁吉亚开设个人银行账户；香港银行账户开户与日常使用完整指南V1.5 |
| 1244 | `04_工作与生活 / 职业、政务与金融` | 量化金融与交易工具 | 1 | `金融、投资、税务与信用` | `small:quantitative-finance` | BigQuant QuantChat AI 量化知识库使用文档 |
| 1245 | `04_工作与生活 / 职业、政务与金融` | 银行金融服务 | 1 | `金融、投资、税务与信用` | `small:banking` | 中国工商银行个人网上银行登录入口 |
| 1246 | `04_工作与生活 / 职业、政务与金融` | 香港银行开户 | 2 | `金融、投资、税务与信用` | `small:hong-kong-banking` | V2EX香港银行卡开户经验与流程总结讨论；香港多家银行开户与虚拟银行开卡实战经验分享 |

## 待测试反馈

1. 请重点抽查“网站、社区与待整理”“成人内容与敏感站点”“云服务、运维与网络安全”等分支中的主题归属是否符合你的使用习惯。
2. 仍有 734 个原本含 3 条及以上书签的叶子目录未动；如果你希望继续精简，我可以先生成“3 条书签叶子目录”的下一轮方案。
3. 迁移后剩余的 7 个含 1–2 条书签目录是新建聚合主题目录，建议先在实际使用中观察，不建议立即再次压平。

---

请在 `/tree` 页面或导出文件中测试新的结构。后续如需继续合并 3 条书签目录、调整某个主题名称或将内容放入 `05_常用`，直接给我反馈即可。
