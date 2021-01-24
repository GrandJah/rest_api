create table employee
(
    id         serial primary key not null,
    first_name varchar(100),
    last_name  varchar(100),
    INN        varchar(12)
);


create table employee_accounts
(
    employee_id int not null references employee,
    accounts_id int not null references person,
    primary key (accounts_id, employee_id)
);
