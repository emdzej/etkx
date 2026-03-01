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

export type PartSearchResult = {
  hauptgruppe: string;
  untergruppe: string;
  sachnummer: string;
  benennung: string;
  zusatz: string;
  benennungKommentar: string;
  btZeilenAlter: string;
  pos: number | null;
  btNummer: string;
  teilDiebstahlrelevant: string;
};

export type PartReplacement = {
  hg: string;
  ug: string;
  sachnummer: string;
  sachnummerAlt: string;
  hgAlt: string;
  ugAlt: string;
  at: string;
  benennung: string;
  zusatz: string;
  si: string;
  lzb: string;
  pi: string;
  benKommentarId: string;
  reach: string;
  aspg: string;
  tc: string;
  teilDiebstahlrelevant: string;
};

export type PartUsage = {
  hg: string;
  ug: string;
  marke: string;
  produktart: string;
  ben: string;
  teilDiebstahlrelevant: string;
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

export type DiagramLineFzg = {
  bildnummer: string;
  teilHg: string;
  teilUg: string;
  teilSachnummer: string;
  teilBenennung: string;
  teilZusatz: string;
  teilEntfall: string;
  teilKommentarId: string;
  teilKommentar: string;
  teilKommPi: string;
  teilSi: string;
  teilReach: string;
  teilAspg: string;
  teilStecker: string;
  teilDiebstahlrelevant: string;
  siDokArt: string;
  teilTc: string;
  teilTcProdDatRelevant: string;
  grpPa: string;
  grpHg: string;
  grpUg: string;
  grpLfdNr: string;
  menge: string;
  katKz: string;
  getriebeKz: string;
  lenkungKz: string;
  einsatz: string;
  auslauf: string;
  bedingungKz: string;
  kommBt: string;
  kommVor: string;
  kommNach: string;
  satzSachnummer: string;
  gruppeId: string;
  blockNr: string;
  bnbBenText: string;
  pos: string;
  btzAlter: string;
  teilBedkezPg: string;
  bedingungArt: string;
  bedingungAlter: string;
};

export type DiagramLineUgb = {
  bildnummer: string;
  teilHg: string;
  teilUg: string;
  teilSachnummer: string;
  teilBenennung: string;
  teilZusatz: string;
  teilEntfall: string;
  teilKommentarId: string;
  teilKommentar: string;
  teilKommPi: string;
  teilSi: string;
  teilReach: string;
  teilAspg: string;
  teilStecker: string;
  teilDiebstahlrelevant: string;
  siDokArt: string;
  teilTc: string;
  teilTcProdDatRelevant: string;
  mmg: string;
  emg: string;
  einsatz: string;
  auslauf: string;
  kommBt: string;
  kommVor: string;
  kommNach: string;
  satzSachnummer: string;
  gruppeId: string;
  blockNr: string;
  bnbBenText: string;
  pos: string;
};

export type DiagramCpLine = {
  pos: string;
  typ: string;
  werk: string;
  art: string;
  datum: string;
  vin: string;
  vinProddatum: string;
  vinMin: string;
  vinMax: string;
  artNummer: string;
  nummer: string;
  cpAlter: string;
};

export type DiagramLine = DiagramLineFzg | DiagramLineUgb | DiagramCpLine;

export type DiagramLinesResponse = {
  vehicleLines: DiagramLineFzg[];
  ugbLines: DiagramLineUgb[];
  cpLines: DiagramCpLine[];
};

export type MainGroup = {
  hauptgruppe: string;
  benennung: string;
};

export type SubGroup = {
  hauptgruppe: string;
  funktionsgruppe: string;
  benennung: string;
};
