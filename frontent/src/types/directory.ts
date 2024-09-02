import { LocalUser } from "./LocalUser";

export interface Directory {
  name: String | null;
  id: Number |  null;
  isPublic: Boolean | null;
  localUser: LocalUser | null;
  createdAt:  string | null;
}
