Goal:
Bunuh semua player lawan, atau miliki poin terbanyak saat dalam jumlah turn tertentu

Breakdown points (tentatif)
>> Poin bunuh player: 200
   * dilihat dari bom-nya siapa yang ngebunuh player itu
   * bunuh diri poinnya nggak dihitung
>> Poin ancurin balok: 10
>> Poin ngambil powerup: 50

Game rules:
[1] Semua player di-spawn pada suatu cell tertentu di map
    State awal: bomb-count=1, bomb-range=1
    (bomb-count adalah jumlah bom si player itu yang bisa berada di papan dalam suatu waktu.
    taruh 1 bomb: <bomb-count> - 1; bomb-nya udah meledak 1: <bomb-count> + 1)
[2] Action tiap turn: gerak satu langkah ke U/D/L/R, diam, atau taruh bom
[3] Bom yang ditaruh punya data (1) siapa yang naruh, (2) kekuatannya berapa, (3) berapa turn lagi
    bakal meledak. Setiap bomb yang ditaruh punya count:8 (baru kelihatan di input buat turn berikutnya).
    Setiap turn, count-nya berkurang 1. Kalau count-nya udah sampai 1, next turn dia akan meledak.
[4] 2 atau lebih player bisa berada di cell yang sama. Kalau ada 2 player yang naruh bom barengan di
    cell tersebut, bomb yang di-spawn tetep cuma 1 (tapi dianggap milik mereka bersama, jadi masing-
    masing kehilangan <bomb-count>, tapi kalau udah meledak masing-masing dapat <bomb-count>-nya lagi).
    Bomb-yang di-spawn tersebut ngambil bomb-range tertinggi dari player-player yang naruh bomb tersebut.
    {NOTE: data kepemilikan bom nggak dimunculin di input, cuma disimpan di server}
[5] Ilustrasi mekanisme BOM meledak: (.)=kosong, (#)=tembok-keras, (x)=tembok-biasa, (F)=Flare
    Asumsikan BOM punya kekuatan 2. Perhatikan kalau ledakan 1 bom nggak bisa tembus multiple tembok-biasa
    [.][.][.][x][.][.][.]    [.][.][.][x][.][.][.]    [.][.][.][x][.][.][.]
    [#][#][#][x][.][.][.]    [#][#][#][F][.][.][.]    [#][#][#][.][.][.][.]
    [.][.][.][B][.][#][.] -> [.][F][F][F][F][#][.] -> [.][.][.][.][.][#][.]
    [.][.][.][.][.][.][.]    [.][.][.][F][.][.][.]    [.][.][.][.][.][.][.]
    [.][.][.][.][.][.][.]    [.][.][.][F][.][.][.]    [.][.][.][.][.][.][.]
[6] Di daerah di mana bom meledak, bakal muncul Flare yang stays di field selama 2 putaran.
    Jadi urutannya: [Bomb/count:2] -> [Bomb/count:1] -> [Flare/time:2] -> [Flare/time:1] -> [Kosong]
    Kalau kebetulan ada bom yang kena flare, maka secara otomatis count-nya akan jadi 1 (langsung meledak
    next turn). Kalau ada kotak flare kena flare lagi, maka nilai <time>-nya ngambil yang tertinggi (max=2)



Sketch input (diberikan di setiap turn):
[1] Baris pertama:
    TURN <turn-ke-berapa>
[2] Baris selanjutnya:
    PLAYER <N=jumlah-player>
[3] N barus berikutnya:
    <player-index> <player-id> Bomb:<bomb-bisa-ditaruh>/<max> Range:<bomb-power> <status> <score>
[4] Baris berikutnya:
    BOARD <H=tinggi> <L=lebar>
[5] H baris berikutnya:
    <representasi-board>
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
TURN 0
PLAYER 4
P0 1306382032 Bomb:1/1 Range:2 Active 0
P1 1306382032 Bomb:0/1 Range:3 Dead 0
P2 1306382032 Bomb:1/1 Range:2 Dead 0
P3 1306382032 Bomb:1/1 Range:2 Active 10
BOARD 11 13
[ ### ][ ### ][  1  ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][ ### ][     ][     ][     ][ B21 ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][  2  ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ][     ]
[ ### ][ ### ][     ][     ][  0  ][     ][     ][     ][     ][     ][     ][     ][     ]
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



Keterangan buat input:
[1] Kalau isinya kurang dari lima karakter, bakal gw kasih padding
    (intinya spasi merupakan karakter yang tidak penting, bisa dibuang dulu spasinya
    sebelum di-parse)
[2] Kalau ada lebih dari 2 brang di satu tempat, bakal dipisahin titik-koma, contoh:
    [ ### ][  1  ][2;4;3][ ### ]
    [     ][ XXX ][F2;+B][     ]
    [     ][     ][ ### ][ XBX ]
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
[     ]: None
[  0  ]: Player 0
[ B21 ]: Bomb power:2/count:1 -> (pas ditaruh count:8; kalau udah count:1 next turn meledak)
[ F2  ]: Flare time:2 -> (pas bomb meledak time:2; kalau udah time:1; next turn hilang)
[ ### ]: Indestructible wall
[ XXX ]: Destructible wall
[ +B  ]: Powerup Bomb+ (jumlah bomb yang bisa ditaruh meningkat)
[ +P  ]: Powerup Power+ (jumlah power/range bomb meningkat 1)
[ XBX ]: Destructible wall with Bomb+
[ XPX ]: Destructible wall with Power+
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



Possible outputs:
[1] Tanda ">>" wajib, kalau nggak ada itu diabaikan (bisa buat debugging)
[2] Kalau move yang illegal akan langsung di-parse jadi "STAY" sama server
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
>> STAY
>> MOVE LEFT
>> MOVE RIGHT
>> MOVE UP
>> MOVE DOWN
>> DROP BOMB
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%





Oretan gw doang:
(ini buat bikin map-nya)
1: PLAYER
#: INDEST WALL
X: DEST WALL
B: PUP-BOMB
P: PUP-POWER
.: EMPTY
