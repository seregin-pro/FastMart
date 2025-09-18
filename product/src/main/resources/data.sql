--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `sku`, `quantity`, `image`, `name`, `description`, `manufacturer_id`, `price`, `weight`, `status`, `viewed`, `date_added`) VALUES
(100, 10000, 10, 'catalog/demo/htc_touch_hd_1.jpg', 'Apple Cinema 30', 'Display that produces flicker-free images that deliver twice the brightness,', 5, 500, 100, 0, 1939, '2023-02-03 16:06:50'),
(200, 10009, 610, 'catalog/demo/htc_touch_hd_1.jpg', 'iPod Classic', 'With 80GB or 160GB deliver twice the brightness of storage and up to,', 10, 900, 700, 5, 939, '2023-12-10 16:06:50');

--
-- Dumping data for table `manufacturer`
--

INSERT INTO `manufacturer` (`id`, `name`, `image`) VALUES
(5, 'HTC', 'catalog/demo/htc_logo.jpg'),
(10, 'Sony', 'catalog/demo/sony_logo.jpg');

--
-- Dumping data for table `oc_category`
--

INSERT INTO `category` (`id`, `image`, `name`, `parent_id`, `status`) VALUES
(5, 'catalog/demo/compaq_presario.jpg', 'Smartphones', 0, 1),
(10, 'catalog/demo/compaq_presario.jpg', 'Computers', 0, 1);

--
-- Dumping data for table `oc_product_to_category`
--

INSERT INTO `product_category` (`product_id`, `category_id`) VALUES
(100, 5),
(200, 10);