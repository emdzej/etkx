export type SeriesParams = {
  marke: string;
  produktart: string;
  katalogumfang: string;
  regionen: string[];
  iso?: string;
  regiso?: string;
  bauart?: string;
  lenkung?: string;
};

export type TypesParams = {
  mospId: string;
  prodDatum: string;
  produktart?: string;
  lenkung?: string;
  getriebe?: string;
  iso?: string;
  regiso?: string;
};

export type VehicleIdentification = {
  modellspalte: string;
  typ: string;
  werk: string;
  marke: string;
  produktart: string;
  katalogumfang: string;
  baureihe: string;
  extBaureihe: string;
  bauart: string;
  extBauart: string;
  karosserie: string;
  extKarosserie: string;
  motor: string;
  modell: string;
  region: string;
  lenkung: string;
  getriebe: string;
  produktionsdatum: string;
  sichtschutz: string;
  einsatz: string;
};

export type Series = {
  baureihe: string;
  extBaureihe: string;
  pos: string;
};

export type Karosserie = {
  karosserie: string;
  extKarosserie: string;
};

export type Modell = {
  modell: string;
  pos: string;
};

export type Region = {
  region: string;
};

export type Lenkung = {
  lenkung: string;
  extLenkung: string;
};

export type Getriebe = {
  getriebe: string;
  extGetriebe: string;
};

export type Baujahr = {
  baujahr: string;
};

export type Zulassungsmonat = {
  zulassungsmonat: string;
  extZulassungsmonat: string;
};

export type MospId = {
  mospId: string;
};

export type VehicleType = {
  typ: string;
};

export type PartSearchLine = {
  hauptgruppe: string;
  untergruppe: string;
  sachnummer: string;
  benennung: string;
  zusatz: string;
  btNummer: string;
  pos: number;
};

export type PartReplacement = {
  sachnummer: string;
  sachnummerAlt: string;
  benennung: string;
  zusatz: string;
  at: string;
};

export type PartUsageVehicle = {
  baureihe: string;
  modell: string;
  karosserie: string;
  einsatz: string;
  auslauf: string;
};

export type DiagramHotspot = {
  bildnummer: string;
  topLeftX: string;
  topLeftY: string;
  bottomRightX: string;
  bottomRightY: string;
};

export type DiagramComment = {
  kommId: string;
  text: string;
  code: string;
  vz: string;
  darstellung: string;
  tiefe: string;
  pos: string;
};

export type DiagramCommentShort = {
  kommId: string;
  text: string;
  pos: string;
};

export type DiagramCondition = {
  kuerzel: string;
  gesamttermVz: string;
  gesamtterm: string;
  og: string;
  vArt: string;
  fZeile: string;
  elementVz: string;
  elementId: string;
  pos: string;
};

export type DiagramOverCondition = {
  kuerzel: string;
  kuerzelUeber: string;
};

export type DiagramReference = {
  bildtafelnummer: string;
  ueberschrift: string;
  text: string;
  pos: string;
};

export type DiagramYesNoText = {
  bezeichnung: string;
  benennung: string;
};

export type DiagramDetails = {
  hotspots: DiagramHotspot[];
  vehicleComments: DiagramComment[];
  ugbComments: DiagramCommentShort[];
  conditions: DiagramCondition[];
  overConditions: DiagramOverCondition[];
  references: DiagramReference[];
  yesNoTexts: DiagramYesNoText[];
};

export type DiagramLine = {
  bildnummer: string;
  teilSachnummer: string;
  teilBenennung: string;
  teilZusatz: string;
  menge: string;
  einsatz: string;
  auslauf: string;
  pos: string;
};

export type DiagramLines = {
  vehicleLines: DiagramLine[];
  ugbLines: any[];
  cpLines: any[];
};

export type MainGroup = {
  hg: string;
  name: string;
  thumbnailId: number | null;
};

export type SubGroup = {
  hg: string;
  fg: string;
  name: string;
  thumbnailId: number | null;
  btnr: string;
};
