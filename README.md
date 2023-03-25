
# Inventory System 

Acesta este proiectul meu pentru cursul de programare avansata pe obiecte, anul 2, semestrul 2, 2022-2023. Scopul programului este sa administreze stocul unei companii si bazele de date care tin informatiile angajatilor si clientilor.  

## Clase, interfete, enums

- `Persoana` - clasa care modeleaza o persoana 
  - `Angajat` - clasa care modeleaza un angajat, mosteneste clasa `Persoana`

- `Repository` - interfata care modeleaza un repository in care sunt tinute date si implementate operatii CRUD
  - `CrudRepository` - clasa abstracta care implementeaza o parte din operatiile CRUD
    - `AngajatRepository` - clasa care modeleaza un repository de angajati
    - `CompanieRepository` - clasa care modeleaza un repository de companii, din care vor fi separate datele pentru clienti si furnizori
    - `ClientiRepository` - clasa care modeleaza un repository de clienti
    - `FurnizoriRepository` - clasa care modeleaza un repository de furnizori
    - `ProdusRepository` - clasa care modeleaza un repository de produse
    - `TranzactieRepository` - clasa care modeleaza un repository de tranzactii
    - `DepoziteRepository` - clasa care modeleaza un repository de depozite

- `Companie` - clasa care modeleaza o companie 
- `Depozit` - clasa care modeleaza un depozit
- `Tranzactie` - clasa care modeleaza o tranzactie
- `Produs` - clasa care modeleaza un produs
- `TipTranzactie` - enum pentru tranzactiile de intrare si de iesire 
- `Utilities` - clasa cu metode utilitare statice 
- `Application` - clasa serviciu care contine operatiile care pot fi efectuate in sistem
- `Main` - clasa prin care se executa programul

## Functionalitati

1. Operatii produse
   1. Adaugare produs
   2. Cautare produs dupa id/obiect
   3. Stergere produs
   4. Modificare produs
   5. Lista produse
2. Operatii angajati
    1. Adaugare angajat
    2. Cautare angajat dupa id/obiect
    3. Stergere angajat
    4. Modificare angajat
    5. Lista angajati
3. Operatii tranzactii
    1. Adaugare tranzactie (intrare/iesire)
    2. Cautare tranzactie dupa id/obiect
    3. Anulare tranzactie (stergerea nu este permisa)
    4. Modificare tranzactie
    5. Lista tranzactii fara filtre sau cu filtre dupa perioada, parteneri, produs, depozite, tip
4. Operatii depozite
   1. Adaugare depozit
   2. Cautare depozit dupa id/obiect
   3. Stergere depozit (daca nu exista tranzactii)
   4. Modificare depozit
   5. Lista depozite
   6. Obtinere stocuri cantitative/valorice pentru un depozit
5. Operatii clienti
    1. Adaugare client
    2. Cautare client dupa id/obiect
    3. Stergere client
    4. Modificare client
    5. Lista clienti
    6. Operatii clienti
6. Operatii furnizori
    1. Adaugare furnizor
    2. Cautare furnizor dupa id/obiect
    3. Stergere furnizor
    4. Modificare furnizor
    5. Lista furnizori
    6. Operatii furnizori