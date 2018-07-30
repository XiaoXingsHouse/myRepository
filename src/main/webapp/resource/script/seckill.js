//存放交互逻辑的js
var seckill = {
    //封装秒杀相关ajax地址
    URL: {
        now: function () {
            return '/seckill/time/now'
        },
        exposer : function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution : function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';

        }
    },
    //处理秒杀逻辑
    handleSeckill : function(seckillId, node){
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function(result){
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //秒杀已经开始
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);

                    //one表示只绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //禁用按钮
                        $(this).addClass('disabled');
                        //发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                }else{
                    //秒杀未开始（预防客户端时间偏差）
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId, now, start, end);
                }
            }
        });
    },
    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    //倒计时判断
    countdown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束啦！！！');
        } else if (nowTime < startTime) {
            //秒杀未开始，计时
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //控制时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {//倒计时结束的回调
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            //秒杀已经开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证、登录、计时交互
            var killPhone = $.cookie('killPhone');
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];

            if (!seckill.validatePhone(killPhone)) {
                //要求输入手机号
                var killPhoneModal = $('#killPhoneModal');
                //显示弹出层
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //写入cookie
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号出错！！！</label>').show(300);
                    }
                });
            }
            //计时
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var timeNow = result['data'];
                    seckill.countdown(seckillId, timeNow, startTime, endTime);
                }
            });
        }
    }
}