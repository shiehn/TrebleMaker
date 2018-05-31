package com.treblemaker.model;

import com.treblemaker.model.interfaces.IInfluenceable;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "chord_progressions")
public class ChordProgression implements IInfluenceable, Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "song_name")
    private String songName;

    @Column(name = "time_signature")
    private String timeSignature;

    @Column(name = "song_key")
    private String songKey;

    @Column(name = "genre")
    private String genre;

    @Column(name = "chord1")
    private String chord1 = new String("");

    @Column(name = "chord2")
    private String chord2 = new String("");

    @Column(name = "chord3")
    private String chord3 = new String("");

    @Column(name = "chord4")
    private String chord4 = new String("");

    @Column(name = "chord5")
    private String chord5 = new String("");

    @Column(name = "chord6")
    private String chord6 = new String("");

    @Column(name = "chord7")
    private String chord7 = new String("");

    @Column(name = "chord8")
    private String chord8 = new String("");

    @Column(name = "chord9")
    private String chord9 = new String("");

    @Column(name = "chord10")
    private String chord10 = new String("");

    @Column(name = "chord11")
    private String chord11 = new String("");

    @Column(name = "chord12")
    private String chord12 = new String("");

    @Column(name = "chord13")
    private String chord13 = new String("");

    @Column(name = "chord14")
    private String chord14 = new String("");

    @Column(name = "chord15")
    private String chord15 = new String("");

    @Column(name = "chord16")
    private String chord16 = new String("");

    @Column(name = "chord17")
    private String chord17 = new String("");

    @Column(name = "chord18")
    private String chord18 = new String("");

    @Column(name = "chord19")
    private String chord19 = new String("");

    @Column(name = "chord20")
    private String chord20 = new String("");

    @Column(name = "chord21")
    private String chord21 = new String("");

    @Column(name = "chord22")
    private String chord22 = new String("");

    @Column(name = "chord23")
    private String chord23 = new String("");

    @Column(name = "chord24")
    private String chord24 = new String("");

    @Column(name = "chord25")
    private String chord25 = new String("");

    @Column(name = "chord26")
    private String chord26 = new String("");

    @Column(name = "chord27")
    private String chord27 = new String("");

    @Column(name = "chord28")
    private String chord28 = new String("");

    @Column(name = "chord29")
    private String chord29 = new String("");

    @Column(name = "chord30")
    private String chord30 = new String("");

    @Column(name = "chord31")
    private String chord31 = new String("");

    @Column(name = "chord32")
    private String chord32 = new String("");

    @Column(name = "chord33")
    private String chord33 = new String("");

    @Column(name = "chord34")
    private String chord34 = new String("");

    @Column(name = "chord35")
    private String chord35 = new String("");

    @Column(name = "chord36")
    private String chord36 = new String("");

    @Column(name = "chord37")
    private String chord37 = new String("");

    @Column(name = "chord38")
    private String chord38 = new String("");

    @Column(name = "chord39")
    private String chord39 = new String("");

    @Column(name = "chord40")
    private String chord40 = new String("");

    @Column(name = "chord41")
    private String chord41 = new String("");

    @Column(name = "chord42")
    private String chord42 = new String("");

    @Column(name = "chord43")
    private String chord43 = new String("");

    @Column(name = "chord44")
    private String chord44 = new String("");

    @Column(name = "chord45")
    private String chord45 = new String("");

    @Column(name = "chord46")
    private String chord46 = new String("");

    @Column(name = "chord47")
    private String chord47 = new String("");

    @Column(name = "chord48")
    private String chord48 = new String("");

    @Column(name = "chord49")
    private String chord49 = new String("");

    @Column(name = "chord50")
    private String chord50 = new String("");

    @Column(name = "chord51")
    private String chord51 = new String("");

    @Column(name = "chord52")
    private String chord52 = new String("");

    @Column(name = "chord53")
    private String chord53 = new String("");

    @Column(name = "chord54")
    private String chord54 = new String("");

    @Column(name = "chord55")
    private String chord55 = new String("");

    @Column(name = "chord56")
    private String chord56 = new String("");

    @Column(name = "chord57")
    private String chord57 = new String("");

    @Column(name = "chord58")
    private String chord58 = new String("");

    @Column(name = "chord59")
    private String chord59 = new String("");

    @Column(name = "chord60")
    private String chord60 = new String("");

    public ChordProgression() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(String timeSignature) {
        this.timeSignature = timeSignature;
    }

    public String getSongKey() {
        return songKey;
    }

    public void setSongKey(String songKey) {
        this.songKey = songKey;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getChord1() {
        return chord1;
    }

    public void setChord1(String chord1) {
        this.chord1 = chord1;
    }

    public String getChord2() {
        return chord2;
    }

    public void setChord2(String chord2) {
        this.chord2 = chord2;
    }

    public String getChord3() {
        return chord3;
    }

    public void setChord3(String chord3) {
        this.chord3 = chord3;
    }

    public String getChord4() {
        return chord4;
    }

    public void setChord4(String chord4) {
        this.chord4 = chord4;
    }

    public String getChord5() {
        return chord5;
    }

    public void setChord5(String chord5) {
        this.chord5 = chord5;
    }

    public String getChord6() {
        return chord6;
    }

    public void setChord6(String chord6) {
        this.chord6 = chord6;
    }

    public String getChord7() {
        return chord7;
    }

    public void setChord7(String chord7) {
        this.chord7 = chord7;
    }

    public String getChord8() {
        return chord8;
    }

    public void setChord8(String chord8) {
        this.chord8 = chord8;
    }

    public String getChord9() {
        return chord9;
    }

    public void setChord9(String chord9) {
        this.chord9 = chord9;
    }

    public String getChord10() {
        return chord10;
    }

    public void setChord10(String chord10) {
        this.chord10 = chord10;
    }

    public String getChord11() {
        return chord11;
    }

    public void setChord11(String chord11) {
        this.chord11 = chord11;
    }

    public String getChord12() {
        return chord12;
    }

    public void setChord12(String chord12) {
        this.chord12 = chord12;
    }

    public String getChord13() {
        return chord13;
    }

    public void setChord13(String chord13) {
        this.chord13 = chord13;
    }

    public String getChord14() {
        return chord14;
    }

    public void setChord14(String chord14) {
        this.chord14 = chord14;
    }

    public String getChord15() {
        return chord15;
    }

    public void setChord15(String chord15) {
        this.chord15 = chord15;
    }

    public String getChord16() {
        return chord16;
    }

    public void setChord16(String chord16) {
        this.chord16 = chord16;
    }

    public String getChord17() {
        return chord17;
    }

    public void setChord17(String chord17) {
        this.chord17 = chord17;
    }

    public String getChord18() {
        return chord18;
    }

    public void setChord18(String chord18) {
        this.chord18 = chord18;
    }

    public String getChord19() {
        return chord19;
    }

    public void setChord19(String chord19) {
        this.chord19 = chord19;
    }

    public String getChord20() {
        return chord20;
    }

    public void setChord20(String chord20) {
        this.chord20 = chord20;
    }

    public String getChord21() {
        return chord21;
    }

    public void setChord21(String chord21) {
        this.chord21 = chord21;
    }

    public String getChord22() {
        return chord22;
    }

    public void setChord22(String chord22) {
        this.chord22 = chord22;
    }

    public String getChord23() {
        return chord23;
    }

    public void setChord23(String chord23) {
        this.chord23 = chord23;
    }

    public String getChord24() {
        return chord24;
    }

    public void setChord24(String chord24) {
        this.chord24 = chord24;
    }

    public String getChord25() {
        return chord25;
    }

    public void setChord25(String chord25) {
        this.chord25 = chord25;
    }

    public String getChord26() {
        return chord26;
    }

    public void setChord26(String chord26) {
        this.chord26 = chord26;
    }

    public String getChord27() {
        return chord27;
    }

    public void setChord27(String chord27) {
        this.chord27 = chord27;
    }

    public String getChord28() {
        return chord28;
    }

    public void setChord28(String chord28) {
        this.chord28 = chord28;
    }

    public String getChord29() {
        return chord29;
    }

    public void setChord29(String chord29) {
        this.chord29 = chord29;
    }

    public String getChord30() {
        return chord30;
    }

    public void setChord30(String chord30) {
        this.chord30 = chord30;
    }

    public String getChord31() {
        return chord31;
    }

    public void setChord31(String chord31) {
        this.chord31 = chord31;
    }

    public String getChord32() {
        return chord32;
    }

    public void setChord32(String chord32) {
        this.chord32 = chord32;
    }

    public String getChord33() {
        return chord33;
    }

    public void setChord33(String chord33) {
        this.chord33 = chord33;
    }

    public String getChord34() {
        return chord34;
    }

    public void setChord34(String chord34) {
        this.chord34 = chord34;
    }

    public String getChord35() {
        return chord35;
    }

    public void setChord35(String chord35) {
        this.chord35 = chord35;
    }

    public String getChord36() {
        return chord36;
    }

    public void setChord36(String chord36) {
        this.chord36 = chord36;
    }

    public String getChord37() {
        return chord37;
    }

    public void setChord37(String chord37) {
        this.chord37 = chord37;
    }

    public String getChord38() {
        return chord38;
    }

    public void setChord38(String chord38) {
        this.chord38 = chord38;
    }

    public String getChord39() {
        return chord39;
    }

    public void setChord39(String chord39) {
        this.chord39 = chord39;
    }

    public String getChord40() {
        return chord40;
    }

    public void setChord40(String chord40) {
        this.chord40 = chord40;
    }

    public String getChord41() {
        return chord41;
    }

    public void setChord41(String chord41) {
        this.chord41 = chord41;
    }

    public String getChord42() {
        return chord42;
    }

    public void setChord42(String chord42) {
        this.chord42 = chord42;
    }

    public String getChord43() {
        return chord43;
    }

    public void setChord43(String chord43) {
        this.chord43 = chord43;
    }

    public String getChord44() {
        return chord44;
    }

    public void setChord44(String chord44) {
        this.chord44 = chord44;
    }

    public String getChord45() {
        return chord45;
    }

    public void setChord45(String chord45) {
        this.chord45 = chord45;
    }

    public String getChord46() {
        return chord46;
    }

    public void setChord46(String chord46) {
        this.chord46 = chord46;
    }

    public String getChord47() {
        return chord47;
    }

    public void setChord47(String chord47) {
        this.chord47 = chord47;
    }

    public String getChord48() {
        return chord48;
    }

    public void setChord48(String chord48) {
        this.chord48 = chord48;
    }

    public String getChord49() {
        return chord49;
    }

    public void setChord49(String chord49) {
        this.chord49 = chord49;
    }

    public String getChord50() {
        return chord50;
    }

    public void setChord50(String chord50) {
        this.chord50 = chord50;
    }

    public String getChord51() {
        return chord51;
    }

    public void setChord51(String chord51) {
        this.chord51 = chord51;
    }

    public String getChord52() {
        return chord52;
    }

    public void setChord52(String chord52) {
        this.chord52 = chord52;
    }

    public String getChord53() {
        return chord53;
    }

    public void setChord53(String chord53) {
        this.chord53 = chord53;
    }

    public String getChord54() {
        return chord54;
    }

    public void setChord54(String chord54) {
        this.chord54 = chord54;
    }

    public String getChord55() {
        return chord55;
    }

    public void setChord55(String chord55) {
        this.chord55 = chord55;
    }

    public String getChord56() {
        return chord56;
    }

    public void setChord56(String chord56) {
        this.chord56 = chord56;
    }

    public String getChord57() {
        return chord57;
    }

    public void setChord57(String chord57) {
        this.chord57 = chord57;
    }

    public String getChord58() {
        return chord58;
    }

    public void setChord58(String chord58) {
        this.chord58 = chord58;
    }

    public String getChord59() {
        return chord59;
    }

    public void setChord59(String chord59) {
        this.chord59 = chord59;
    }

    public String getChord60() {
        return chord60;
    }

    public void setChord60(String chord60) {
        this.chord60 = chord60;
    }
}
