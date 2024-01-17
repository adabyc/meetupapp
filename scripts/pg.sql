CREATE TABLE items
(
    id			serial primary key,
    name        VARCHAR(40) not null,
    flag        BOOL
);


insert into items(name, flag) values('name1', true);
select * from items;