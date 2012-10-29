# Database Structure #

+ CREATE TABLE Attribute (id integer generated by default as identity not null primary key, name varchar(255), type int)
+ CREATE TABLE NodeType (id integer generated by default as identity not null primary key, name varchar(255))
+ CREATE TABLE Node (id integer generated by default as identity not null primary key, nodeType integer)
+ CREATE TABLE NodeAttrValue (nodeId integer, attrId integer, value varchar(255))