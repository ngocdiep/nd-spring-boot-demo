create sequence hibernate_sequence
start 1 increment 1;

create table leaf
(
    id int8 not null,
    time_stamp_created int8,
    time_stamp_updated int8,
    version int8 not null,
    name varchar(255),
    node_id int8,
    primary key (id)
);

create table node
(
    id int8 not null,
    time_stamp_created int8,
    time_stamp_updated int8,
    version int8 not null,
    name varchar(255),
    parent_path ltree,
    parent_id int8,
    primary key (id)
);

alter table leaf add constraint FK_parent_node_id foreign key (node_id) references node;
alter table node add constraint FK_parent_id foreign key (parent_id) references node;