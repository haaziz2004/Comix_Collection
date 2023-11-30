// Example model schema from the Drizzle docs
// https://orm.drizzle.team/docs/sql-schema-declaration

import { relations } from "drizzle-orm";
import {
  bigint,
  boolean,
  datetime,
  float,
  index,
  int,
  mysqlEnum,
  mysqlTableCreator,
  primaryKey,
  text,
  unique,
  varchar,
} from "drizzle-orm/mysql-core";

/**
 * This is an example of how to use the multi-project schema feature of Drizzle ORM. Use the same
 * database instance for multiple projects.
 *
 * @see https://orm.drizzle.team/docs/goodies#multi-project-schema
 */
export const mysqlTable = mysqlTableCreator((name) => `comix_${name}`);

export const users = mysqlTable("users", {
  id: bigint("id", { mode: "number" }).primaryKey().autoincrement(),
  username: varchar("username", { length: 255 }).unique().notNull(),
  password: varchar("password", { length: 255 }).notNull(),
  role: mysqlEnum("role", ["ADMIN", "USER"]).default("USER").notNull(),
});

export const usersRelations = relations(users, ({ many }) => ({
  userComics: many(userComics),
}));

export const comics = mysqlTable(
  "comics",
  {
    id: bigint("id", { mode: "number" }).primaryKey().autoincrement(),
    publisher: varchar("publisher", { length: 40 }).notNull(),
    seriesTitle: varchar("series_title", { length: 255 }).notNull(),
    volumeNumber: varchar("volume_number", { length: 36 }).notNull(),
    issueNumber: varchar("issue_number", { length: 10 }).notNull(),
    publicationDate: datetime("publication_date").notNull(),
    storyTitle: varchar("story_title", { length: 255 }),
    description: text("description"),
  },
  (t) => ({
    unq: unique("comic_unique").on(
      t.publisher,
      t.seriesTitle,
      t.volumeNumber,
      t.issueNumber,
      t.publicationDate,
    ),
  }),
);

export const comicsRelations = relations(comics, ({ many }) => ({
  comicsToComicCreators: many(comicsToComicCreators),
}));

export const userComics = mysqlTable(
  "user_comics",
  {
    id: bigint("id", { mode: "number" }).primaryKey().autoincrement(),
    publisher: varchar("publisher", { length: 40 }).notNull(),
    seriesTitle: varchar("series_title", { length: 255 }).notNull(),
    volumeNumber: varchar("volume_number", { length: 36 }).notNull(),
    issueNumber: varchar("issue_number", { length: 10 }).notNull(),
    publicationDate: datetime("publication_date").notNull(),
    storyTitle: varchar("story_title", { length: 255 }),
    description: text("description"),

    value: float("value").notNull().default(0),
    grade: int("grade").notNull().default(0),
    slabbed: boolean("slabbed").notNull().default(false),

    userId: bigint("user_id", { mode: "number" }).notNull(),
  },
  (t) => ({
    unq: unique("user_comic_unique").on(
      t.publisher,
      t.seriesTitle,
      t.volumeNumber,
      t.issueNumber,
      t.publicationDate,
      t.userId,
    ),
  }),
);

export const userComicsRelations = relations(userComics, ({ one, many }) => ({
  user: one(users, {
    fields: [userComics.userId],
    references: [users.id],
  }),
  userComicsToUserComicCreators: many(userComicsToUserComicCreators),
  userComicsToUserComicCharacters: many(userComicsToUserComicCharacters),
}));

export const comicCreators = mysqlTable(
  "comic_creators",
  {
    id: bigint("id", { mode: "number" }).primaryKey().autoincrement(),
    name: varchar("name", { length: 255 }).notNull().unique(),
  },
  (t) => ({
    nameIdx: index("name_index").on(t.name),
  }),
);

export const comicCreatorsRelations = relations(comicCreators, ({ many }) => ({
  comicsToComicCreators: many(comicsToComicCreators),
}));

export const comicsToComicCreators = mysqlTable(
  "comics_to_comic_creators",
  {
    comicId: int("comic_id").notNull(),
    comicCreatorId: int("comic_creator_id").notNull(),
  },
  (t) => ({
    pk: primaryKey({
      name: "comic_id_to_comic_creator_id_pk",
      columns: [t.comicId, t.comicCreatorId],
    }),
  }),
);

export const comicsToComicCreatorsRelation = relations(
  comicsToComicCreators,
  ({ one }) => ({
    comic: one(comics, {
      fields: [comicsToComicCreators.comicId],
      references: [comics.id],
    }),
    comicCreator: one(comicCreators, {
      fields: [comicsToComicCreators.comicCreatorId],
      references: [comicCreators.id],
    }),
  }),
);

export const userComicCreators = mysqlTable(
  "user_comic_creators",
  {
    id: bigint("id", { mode: "number" }).primaryKey().autoincrement(),
    name: varchar("name", { length: 255 }).notNull(),
  },
  (t) => ({
    nameIdx: index("name_index").on(t.name),
  }),
);

export const userComicCreatorsRelations = relations(
  userComicCreators,
  ({ many }) => ({
    userComicsToComicCreators: many(userComicsToUserComicCreators),
  }),
);

export const userComicsToUserComicCreators = mysqlTable(
  "user_comics_to_user_comic_creators",
  {
    userComicId: int("user_comic_id").notNull(),
    userComicCreatorId: int("user_comic_creator_id").notNull(),
  },
  (t) => ({
    pk: primaryKey({
      name: "user_comic_id_user_comic_creator_id",
      columns: [t.userComicId, t.userComicCreatorId],
    }),
  }),
);

export const userComicsToComicCreatorsRelation = relations(
  userComicsToUserComicCreators,
  ({ one }) => ({
    userComic: one(userComics, {
      fields: [userComicsToUserComicCreators.userComicId],
      references: [userComics.id],
    }),
    userComicCreator: one(userComicCreators, {
      fields: [userComicsToUserComicCreators.userComicCreatorId],
      references: [userComicCreators.id],
    }),
  }),
);

export const userComicCharacters = mysqlTable(
  "user_comic_characters",
  {
    id: bigint("id", { mode: "number" }).primaryKey().autoincrement(),
    name: varchar("name", { length: 255 }).notNull(),
  },
  (t) => ({
    nameIdx: index("name_index").on(t.name),
  }),
);

export const userComicCharactersRelations = relations(
  userComicCharacters,
  ({ many }) => ({
    userComicsToUserComicCharacter: many(userComicsToUserComicCharacters),
  }),
);

export const userComicsToUserComicCharacters = mysqlTable(
  "user_comics_to_user_comic_characters",
  {
    userComicId: int("user_comic_id").notNull(),
    userComicCharacterId: int("user_comic_character_id").notNull(),
  },
  (t) => ({
    pk: primaryKey({
      name: "user_comic_id_user_comic_character_id",
      columns: [t.userComicId, t.userComicCharacterId],
    }),
  }),
);

export const userComicsToUserComicCharactersRelation = relations(
  userComicsToUserComicCharacters,
  ({ one }) => ({
    userComic: one(userComics, {
      fields: [userComicsToUserComicCharacters.userComicId],
      references: [userComics.id],
    }),
    userComicCharacter: one(userComicCharacters, {
      fields: [userComicsToUserComicCharacters.userComicCharacterId],
      references: [userComicCharacters.id],
    }),
  }),
);
