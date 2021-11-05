if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end

tonumber(arg[, base]) - 若参数能转为数字则返回一个数值.可以指定转换的类型.默认为十进制整数