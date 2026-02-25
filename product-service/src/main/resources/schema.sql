-- Table structure for table product_category

DROP TABLE IF EXISTS product_category;

CREATE TABLE product_category (
    product_id integer NOT NULL,
    category_id integer NOT NULL,
    PRIMARY KEY(product_id, category_id)
);

-- Table structure for table product

DROP TABLE IF EXISTS product;

CREATE TABLE product (
   id SERIAL PRIMARY KEY,
   sku varchar(64) NOT NULL,
   quantity integer NOT NULL DEFAULT 0,
   image varchar(255) DEFAULT NULL,
   name TEXT DEFAULT NULL,
   description TEXT DEFAULT NULL,
   manufacturer_id integer NOT NULL,
   price decimal(15, 4) NOT NULL DEFAULT 0.0000,
   weight decimal(15, 8) NOT NULL DEFAULT 0.00000000,
   status integer NOT NULL DEFAULT 0,
   viewed integer NOT NULL DEFAULT 0,
   date_added timestamp without time zone NOT NULL
);

-- Table structure for table manufacturer

DROP TABLE IF EXISTS manufacturer;

CREATE TABLE manufacturer (
    id SERIAL PRIMARY KEY,
    name varchar(64) NOT NULL,
    image varchar(255) DEFAULT NULL
);

-- Table structure for table category

DROP TABLE IF EXISTS category;

CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    image varchar(255) DEFAULT NULL,
    name TEXT DEFAULT NULL,
    parent_id integer NOT NULL DEFAULT 0,
    status integer NOT NULL
);