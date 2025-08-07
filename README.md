# EEGProcessor

## Technikai részletek

- **Fejlesztői környezet:** IntelliJ IDEA
- **UI:** Swing
- **Java verzió:** JDK 21  
- **Futtatható főosztály:** `src/Main.java`

## Kimeneti fájlok

- Az első sikeres futtatás után az `output_files/` mappa automatikusan létrejön
- Minden feldolgozott fájl bekerül egy egyedi mappába, ami a következő formátummal rendelkezik: `[bemeneti_fájl_név]__[dátum_idő]`
- A fájlnevek formátuma: `channel[1-32].csv`
- Minden fájl két oszlopot tartalmaz: az átlagban lévő minták indexe (pl.: [1-2-3]) és az átlagolt érték.

## Funkciók

- Feldolgozás indítása, szüneteltetése, folytatása és leállítása a grafikus felületen keresztül.
- A feldolgozás állapotát státuszüzenet jelzi a felületen.

