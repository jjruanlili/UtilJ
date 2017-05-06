-- mysql
-- 如果id需要使用字母，可以再写一个拼接字符的CONCAT，也可以在程序中处理
-- 如果把带字符的id存表中，则需要先去掉字符再+1，感觉比较不合理

-- 建表
drop table if exists singleNumber;
create table singleNumber(  
  Id int AUTO_INCREMENT not null  
  ,Name varchar(100) not null comment '表名'  
  ,Num bigint not null default 0 comment '该表对应的编号'  
  ,primary key `P_Id`(`Id`)  
  ,unique key `U_Name`(`Name`)  
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='编号表';

-- 获取时间戳的存储过程
DROP PROCEDURE IF EXISTS getTodayFirst;
CREATE PROCEDURE getTodayFirst(OUT str VARCHAR(20))
BEGIN
-- 当天时间 年月日
    SET @tmp:=replace(DATE(NOW()),"-","");
--  后面拼接 0000001
		set str = CONCAT(@tmp,'0000001');
END;

--  生成存储过程
 DROP PROCEDURE IF EXISTS getSingleNumber;
-- 名字叫 getSingleNumber
-- 输入参数m对应需要查询的表名  输出参数n最新的编号
CREATE PROCEDURE getSingleNumber(in m varchar(100),out n bigint)
begin
-- 判断是否已存在
set @v:= (select exists (select Num from singleNumber where Name=m));
if @v<>1 then
-- 不存在则初始化
set @f:="";
-- 获取初始化时间戳
call getTodayFirst(@f);
insert into singleNumber(Name,Num)values(m,@f);
else
update singleNumber set Num=Num+1 where Name=m;
end if;
select Num into n from singleNumber where Name=m;
-- set n=(select Num from singleNumber where Name=m);
end;

-- 调用
call getSingleNumber('tmp',@lastNumber);
select @lastNumber;

-- 调用
call getSingleNumber('tmp',@lastNumber);
select @lastNumber;

-- 调用
call getSingleNumber('tmp2',@lastNumber);
select @lastNumber;