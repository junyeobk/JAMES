select IRUM,ADDR from kh

create table kh91(
irum varchar(100),
addr varchar(500)
)
insert into kh91(irum,addr) values('kh당산','서울특별시영등포구당산')
select irum,addr from kh91