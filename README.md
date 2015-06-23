# ArduinoBluetoothRobot
Sterowanie robotem za pomocą android, arduino, czujniki światła i dźwieku

Celem projektu było zbudowanie platformy mobilnej oraz przejęcie kontroli nad jej ruchem na trzy rożne sposoby, w oparciu o następujące sensory:
- akcelerometr, będący elementem wyposażenia urządzenia mobilnego, zarządzanego przez system operacyjny Android,
-  czujnik ultradźwiękowy, zamontowany bezpośrednio na pojeździe i  współpracujący  z platformą Arduino,
- dwa czujniki natężenia światła (fototranzystory), również przytwierdzone do konstrukcji mobilnej, kompatybilne z Arduino.
Konstrukcja samochodu, niezbędna do realizacji założonych  celów została własnoręcznie wykonana. Ruch pojazdu realizowany jest za pośrednictwem dwóch silniczków z przekładniami, zasilanych napięciem maksymalnie 6V. Napęd przenoszony jest niezależnie na dwa zamocowane do podwozia koła, dzięki czemu poprzez zmianę prędkości obrotowej silników, czy też kierunku ich obrotów, możliwe jest przemieszczanie się platformy we wszystkie strony. 
Podstawowym elementem, wchodzącym w skład wyposażenia pojazdu jest płytka uruchomieniowa Arduino UNO R3 z wbudowanym mikrokontrolerem ATMEGA328. 
Kolejnym modułem, w który została wyposażona platforma mobilna jest Motor Shield R3. Jest to płytka, dzięki której można bezpośrednio z programu uruchomionego w Arduino sterować niskonapięciowymi silniczkami na prąd stały.
Oprócz znajdującego się na płytce mikrokontrolera ATMEGA328, do zestawu został dołączony dodatkowy, niezależny układ scalony ATTINY 2313 firmy Atmel. Został on zaprogramowany również przy użyciu Arduino UNO, które może służyć jako programator dzięki załadowaniu do ATMEGI specjalnego programu, oraz dodaniu odpowiedniego pluginu. Do ATTINY został zaimplementowany program, obsługujący zaświecenie się diód - reprezentujących światła wsteczne w pojeździe, oraz włączenie sygnalizacji dźwiękowej podczas jazdy do tyłu - w trybie sterowania akcelerometrem. Zasilanie mikrokontrolera powiązano z jednym z wyjść cyfrowych w Arduino, które w zależności od wskazań akcelerometru osiąga stan wysoki albo niski - czym aktywuje bądź dezaktywuje wykonywanie programu zapisanego w pamięci układu.


Opis do jednego z obrazków:

1. Arduino UNO R3 z mikrokontrolerem ATMEGA328
2. Sterownik silników Arduino Motor Shield R3
3. Fototranzystory
4. Serwomechanizm modelarski HTX900
5. Sonar ultradźwiękowy HC-SR04
6. Moduł BLUETOOTH HC06
7. Płytka z zaprogramowanym układem ATTINY 2313
8. Diody LED reprezentujące światła wsteczne w robocie
9. Głośnik emitujący sygnał dźwiękowy
10. Magazynek na baterie (6 x AA)
11. Dwa silniczki z przekładniami, zasilane napięciem maksymalnie 6V


Więcej informacji: https://drive.google.com/file/d/0B3vtbgnS_S8FQ2dpS2NmN3h5N0E/view?usp=sharing
Kod źródłowy na platformę arduino: https://github.com/olasiewicz/wojtek_car_arduino

Pozdrawiam i życzę dobrej zabawy
W.olasiewicz
