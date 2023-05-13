create table if not exists customers
(
    customerNumber int auto_increment,
    customerName   varchar(50)                                                                                  not null,
    phone          varchar(50)                                                                                  not null,
    addressLine    varchar(1000)                                                                                null,
    `rank`         enum ('Membership', 'Silver', 'Gold', 'Platinum', 'Diamond', 'Emerald') default 'Membership' null,
    customerSSN    mediumtext                                                                                   not null,
    constraint customerName
        unique (customerName, phone),
    constraint customerNumber
        unique (customerNumber)
)
    collate = utf8mb4_unicode_ci;

create table if not exists offices
(
    officeCode  int auto_increment
        constraint `PRIMARY`
        primary key,
    phone       varchar(255) null,
    addressline varchar(255) null,
    region      varchar(100) null
)
    collate = utf8mb4_unicode_ci;

create table if not exists employees
(
    employeeNumber  int auto_increment
        constraint `PRIMARY`
        primary key,
    lastName        varchar(255)                   null,
    firstName       varchar(255)                   null,
    birthDate       date                           null,
    gender          varchar(10)                    null,
    email           varchar(255)                   null,
    mailVerified    tinyint(1)   default 0         not null,
    officeCode      int                            null,
    reportsTo       int                            null,
    jobTitle        varchar(255)                   null,
    username        varchar(100)                   null,
    password        varchar(100) default '123'     not null,
    phoneCode       varchar(30)  default '+84(VN)' not null,
    phone           varchar(20)                    null,
    status          varchar(10)                    null,
    joiningDate     date                           null,
    lastWorkingDate date                           null,
    constraint employees_employees_employeeNumber_fk
        foreign key (reportsTo) references employees (employeeNumber),
    constraint fk_employees_offices
        foreign key (officeCode) references offices (officeCode)
            on update cascade on delete cascade
)
    collate = utf8mb4_unicode_ci;

create table if not exists activitylog
(
    actionID            int auto_increment
        constraint `PRIMARY`
        primary key,
    time                datetime                      not null,
    result              varchar(20) default 'SUCCESS' not null,
    userID              int                           not null,
    description         varchar(200)                  not null,
    componentModifiedID varchar(20)                   null,
    constraint userID
        foreign key (userID) references employees (employeeNumber)
);

create index officeCode
    on employees (officeCode);

create index reportsTo
    on employees (reportsTo);

create table if not exists orders
(
    orderNumber    int auto_increment
        constraint `PRIMARY`
        primary key,
    orderDate      date                                            not null,
    requiredDate   date                                            null,
    shippedDate    date                                            null,
    status         varchar(255)                                    null,
    comments       varchar(255)                                    null,
    customerNumber int                                             not null,
    type           enum ('online', 'onsite') default 'online'      not null,
    value          decimal(10, 2)            default 0.00          null,
    payment_method varchar(50)               default 'Credit card' null,
    created_by     int                                             null,
    deliver_to     varchar(255)                                    null,
    constraint fk_created_by
        foreign key (created_by) references employees (employeeNumber),
    constraint orders_fk_customerNumber
        foreign key (customerNumber) references customers (customerNumber)
            on update cascade on delete cascade
)
    collate = utf8mb4_unicode_ci;

create index customerNumber
    on orders (customerNumber);

create table if not exists productlines
(
    productLine     varchar(50)   not null
        constraint `PRIMARY`
        primary key,
    textDescription varchar(4000) null
)
    collate = utf8mb4_unicode_ci;

create table if not exists products
(
    productCode        varchar(15)    not null
        constraint `PRIMARY`
        primary key,
    productName        varchar(255)   null,
    productLine        varchar(50)    not null,
    productVendor      varchar(255)   null,
    productDescription text           null,
    quantityInStock    int            not null,
    buyPrice           decimal(10, 2) not null,
    sellPrice          decimal(10, 2) not null,
    constraint products_productlines_productLine_fk
        foreign key (productLine) references productlines (productLine)
)
    collate = utf8mb4_unicode_ci;

create table if not exists orderdetails
(
    orderNumber     int            not null,
    productCode     varchar(15)    not null,
    quantityOrdered int            not null,
    priceEach       decimal(10, 2) not null,
    constraint `PRIMARY`
        primary key (orderNumber, productCode),
    constraint orderdetails_ibfk_1
        foreign key (orderNumber) references orders (orderNumber)
            on update cascade on delete cascade,
    constraint orderdetails_products_productCode_fk
        foreign key (productCode) references products (productCode)
)
    collate = utf8mb4_unicode_ci;

create index productCode
    on orderdetails (productCode);

create index productCode
    on products (productCode);

create index productLine
    on products (productLine);

create index sellPrice
    on products (sellPrice);

