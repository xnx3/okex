## okex 操盘辅助
当前只支持以 USDT、BTC 为价格单位进行交易的货币！其中如订单管理、资金计算等，都是自动将BTC换算为USDT进行的记录。

# 声明
## 本项目只是测试Java对接API接口、做代码练习使用，禁止用于非法用途，不得用于国家所不允许的地方。

## 项目运行
1. 直接运行 com.xnx3.okex.Entry 即可看到图形界面。
1. 对接okex的api参数，参考文档 https://github.com/xnx3/okex/tree/master/res/okex_api.pdf
1. 配置好后重新启动项目即可使用。

## 已成交的订单管理
1. 已成交的订单会自动同步到本地进行永久保存数据（不联网也都能看）
1. 成交提醒，当有订单成交后，软件会实时自动播放声音，告知你是什么币，以什么价格成交了。
1. 可以按照成交的时间来进行排序查看最近成交的订单，而非是okex上的只能按照你委托的时间。
1. 只有完全成交的订单才会同步下来，已撤销、未成交的不会同步到已完成订单（进行中的订单有其他功能进行管理）
1. 如果你今天没开电脑，用APP操作的OKEX，第二天你打开电脑，开启本软件后，它会自动同步你昨天已成交的，但还没有同步下来的订单，拉下来保存到你自己电脑。


## 暴涨暴跌自动提醒
1. 可以设置监控某个币，如果这个币出现暴涨或暴跌，幅度达到了在百分之多少，软件就会自动播放声音提醒。
2. 这个暴涨、暴跌的幅度的百分比、以及监控哪个币，都可以自己进行自定义设置。

## 重复下单委托
本功能就是当你委托买入或卖出的币成交后，自动再下委托以同样的价格买入同样的币。  
在自动再下委托时，会监控上次下委托时的最高买价跟最低卖价，如果这两个价一致，那么才会自动下委托单，如果监控到这两个价出现变化，那么就不会再自动委托下单了，免得下错单，重复委托下单功能自动失效。
#### 使用场景:
在有些情况下，有人跟你竞争，比如买一个币，你出 0.01 ，他就出 0.02 ，然后你再出 0.03 ，他就继续出 0.04，导致吧价格拉高，买入价升高。  
这种情况如果是他本来出 0.02 委托买入 10000个币， 而你出 0.03 ，只委托买入 5 个币，你这个小户人家可能根本就不搭理你，不会再跟你竞争价格。这样你就可以一直以 0.03的价格收币了，成交就再自动创建委托

## 计划委托
当某个币的价格低于多少时，自动发出委托，进行买卖操作。监控的币、价格、等，都可以进行自由设定。
1. 如果发出委托后，30分钟内没有成交，那么自动撤销委托，收回被冻结的本金，以供其他币出现底价的时还可以去买。
1. 某个币成功触发委托后，就不再继续委托，而是停止。比如 BTC-USDT到达某个价格触发自动发出委托后，那么BTC-USDT这个币就不在监控委托了。（其他的币依旧还是持续计划委托）
1. 软件关闭后，再打开，上次所有开启的计划委托全部默认是停止状态，只需要调整一下价格，再开启即可。当委托的币种非常多时，非常容易管理。

## 自动扫描计算出当前最低价格的币种
可以设置一定时间内，当前的价格是最低价格的币，给用户一个指引，方便用户购买选择。  
1. 如果再这段时间内，这个币是一路下跌的，那么会自动过滤掉这个币，不会列出来。
1. 可以设置以1分、3分、5分、15分、30分、1小时 等为一个区间，搜索最近100个区间内的，当前是不是最低价。
1. 可以设置当前价格属于最低价格的百分之几

## 暴跌自动交易
整个OKEX中自动搜索以 USDT、BTC 为交易货币的币，如果其中有暴跌超过4%的，那么自动委托下单，以比当前出价多那么一丁点的价格 ，自动出价下单购买。
1. 这个暴跌不是根据K线来的，K线再一些交易量不频繁的币种上，是OKEX系统自动预估的，并不是真实出价。这个是直接根据实际出价委托交易的数据来进行预测。
1. 一些稳定币、以及24小时成交金额小于10000USDT的币种直接淘汰掉，不纳入筛选行列。
1. 接口超时检测，如果因为网络问题导致时间消耗过长(超过75秒)，而造成的系统预测上达到暴跌标准的，将因为耗时超标自动舍弃。
1. 如果某个币出现了暴跌，系统自动对其最近100分钟的K线进行分析，
	1. 当前价格必须低于其他90分钟的价格，也就是在这100分钟区间之中，当前的价格必须是排在最低价的前10个中。
	1. 在这100分钟里，这个币不能是一路跌的，不然即使再暴跌，买了可能也涨不上去
	1. 当前100分钟里，是不是有正常交易量的，如果这100分钟里，有超过40分钟的时间都是无交易量，那么这个币的暴跌可能是庄家人为制造假象要割韭菜
1. 自动委托购买时，会自动根据当前币种的交易量、成交金额等，自动适配购买指定的数量进行下单。比如对于一些日成交量也就2万USDT的币来说，可能一次购买总金额只有2USDT；而日成交量一千万USDT的币，一次委托购买50USDT的。
1. 自动委托出价后，如果2分钟内没有成功买入，那说明当前交易属于停滞状态，或者已经有人以更高价委托下单了，程序自动撤销订单。
1. 如果成功买入了，自动以当前买入价格 + 1.5%的利润委托卖出。（也就是一次成交赚取1.5%的利润）

#### 缺陷
1. 可能会出现两次连续暴跌，非人力所及，无法预估及评测，可能直接就赔5%

## 自动切换能访问的请求节点
软件内置请求节点自动切换功能，实时检测当前节点是否正常，如果不正常，寻找最优节点自动切换连接。
#### 使用场景:
因为网络问题，okex平台在某些地区可能访问有异常，比如访问 www.okexcn.com 可以访问，但访问 www.okex.com 时就访问不了，有时候这俩都访问不了，访问 www.okex.win 又能访问。
如果软件开了一小时后，接口出现异常访问不了了，而用户正在干其他事情，软件就会在用户不知情的情况下罢工了，显然这是不能被用户接受的。而节点智能切换，保障跟OKEX通信正常！


