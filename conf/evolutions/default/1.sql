
# --- !Ups

CREATE TABLE widget (
  id        BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name      VARCHAR(255) NOT NULL,
  created   DATETIME     NOT NULL
);

# --- !Downs

DROP TABLE widget;
