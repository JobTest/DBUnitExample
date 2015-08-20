CREATE TABLE IF NOT EXISTS `address` (
  `street` varchar(45) DEFAULT NULL COMMENT '						',
  `houseNr` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `zipCode` int(11) DEFAULT NULL,
  `person` int(11) NOT NULL,
  PRIMARY KEY (`person`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `address` (`street`, `houseNr`, `city`, `zipCode`, `person`) VALUES('musta', '37', 'tallinn', 12918, 1);



CREATE TABLE IF NOT EXISTS `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(45) DEFAULT NULL,
  `lastname` varchar(45) DEFAULT NULL,
  `mother` int(11) DEFAULT NULL,
  `father` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mother_fk_idx` (`mother`),
  KEY `father_idx` (`father`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;

INSERT INTO `person` (`id`, `firstname`, `lastname`, `mother`, `father`) VALUES
(1, 'george', 'zalizko', NULL, NULL),
(3, 'Natalja', 'Zalizko', NULL, NULL),
(4, 'Alexandra', 'Zalizko', 3, 1),
(5, 'son', 'Zalizko', 3, 1),
(6, NULL, 'her znajet kto', NULL, NULL);



ALTER TABLE `address`
  ADD CONSTRAINT `address_ibfk_1` FOREIGN KEY (`person`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `person`
  ADD CONSTRAINT `father_fk` FOREIGN KEY (`father`) REFERENCES `person` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `mother_fk` FOREIGN KEY (`mother`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
