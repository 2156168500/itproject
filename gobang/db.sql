drop table if exists user;
create table user(
                     userID int primary key auto_increment,
                     username varchar(500) not null unique ,
                     password varchar(500) not null ,
                     score int ,#天梯分数
                     total_count int ,#总场数
                     win_count int
)