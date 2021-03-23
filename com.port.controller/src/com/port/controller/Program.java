package com.port.controller;

import com.port.controller.model.CommandController;
import com.port.controller.model.ConsoleHandler;
import com.port.controller.model.Operator;
import com.port.port.PortController;
import com.port.port.model.StatisticObject;
import com.port.timetable.TimetableGenerator;
import com.port.timetable.model.Ship;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.function.UnaryOperator;

import static com.port.controller.model.ConsoleHandler.ErrorType.*;
import static com.port.controller.model.ConsoleHandler.MessageType.*;

public class Program {
    private static final LinkedList<Ship> timetable = new LinkedList<>();
    private final static Scanner scanner = new Scanner(System.in);
    private final static long time = System.currentTimeMillis();
    private static CommandController controller;
    private static StatisticObject lastStatistic = null;

    public static void main(String[] args) {
        //  long time = System.currentTimeMillis();
        //  final LinkedList<Ship> timetable = TimetableGenerator.generate(time);

        // String json = new Gson().toJson(timetable);
//        final LinkedList<Ship> timetable = new LinkedList<>();
//        String json = "[{\"arriveTime\":1614701534844,\"name\":\"18865\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":916,\"stayTime\":27480000,\"goods\":916,\"workingCranes\":0,\"delay\":910},{\"arriveTime\":1614865641123,\"name\":\"cd668\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":796,\"stayTime\":47760000,\"goods\":1592,\"workingCranes\":0,\"delay\":495},{\"arriveTime\":1614896210662,\"name\":\"19901\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":629,\"stayTime\":9420000,\"goods\":314,\"workingCranes\":0,\"delay\":154},{\"arriveTime\":1614922299215,\"name\":\"57e62\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":929,\"stayTime\":55740000,\"goods\":1858,\"workingCranes\":0,\"delay\":1073},{\"arriveTime\":1615003741749,\"name\":\"32b21\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":941,\"stayTime\":14100000,\"goods\":470,\"workingCranes\":0,\"delay\":1156},{\"arriveTime\":1615027977046,\"name\":\"5a185\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":252,\"stayTime\":3780000,\"goods\":126,\"workingCranes\":0,\"delay\":522},{\"arriveTime\":1615042424559,\"name\":\"f77da\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":897,\"stayTime\":53820000,\"goods\":1794,\"workingCranes\":0,\"delay\":1083},{\"arriveTime\":1615120433254,\"name\":\"36309\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":30,\"stayTime\":1800000,\"goods\":60,\"workingCranes\":0,\"delay\":1302},{\"arriveTime\":1615196499800,\"name\":\"03aff\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":836,\"stayTime\":12540000,\"goods\":418,\"workingCranes\":0,\"delay\":35},{\"arriveTime\":1615377430877,\"name\":\"d8fe0\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":64,\"stayTime\":3840000,\"goods\":128,\"workingCranes\":0,\"delay\":842},{\"arriveTime\":1615425838490,\"name\":\"daeef\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":963,\"stayTime\":57780000,\"goods\":1926,\"workingCranes\":0,\"delay\":953},{\"arriveTime\":1615462493796,\"name\":\"76da7\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":312,\"stayTime\":4680000,\"goods\":156,\"workingCranes\":0,\"delay\":1340},{\"arriveTime\":1615484305511,\"name\":\"92e21\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":890,\"stayTime\":53400000,\"goods\":1780,\"workingCranes\":0,\"delay\":526},{\"arriveTime\":1615546650357,\"name\":\"7d43e\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":863,\"stayTime\":51780000,\"goods\":1726,\"workingCranes\":0,\"delay\":1070},{\"arriveTime\":1615578282996,\"name\":\"101d0\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":329,\"stayTime\":4920000,\"goods\":164,\"workingCranes\":0,\"delay\":369},{\"arriveTime\":1615583352463,\"name\":\"5c2b7\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":833,\"stayTime\":49980000,\"goods\":1666,\"workingCranes\":0,\"delay\":1199},{\"arriveTime\":1615592855479,\"name\":\"3b408\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":86,\"stayTime\":1260000,\"goods\":43,\"workingCranes\":0,\"delay\":1264},{\"arriveTime\":1615664283759,\"name\":\"8bd77\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":740,\"stayTime\":22200000,\"goods\":740,\"workingCranes\":0,\"delay\":349},{\"arriveTime\":1615665330396,\"name\":\"7229e\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":628,\"stayTime\":9420000,\"goods\":314,\"workingCranes\":0,\"delay\":980},{\"arriveTime\":1615676863140,\"name\":\"fece8\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":342,\"stayTime\":10260000,\"goods\":342,\"workingCranes\":0,\"delay\":951},{\"arriveTime\":1615689821115,\"name\":\"8bf1f\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":66,\"stayTime\":3960000,\"goods\":132,\"workingCranes\":0,\"delay\":715},{\"arriveTime\":1615691441519,\"name\":\"03aa9\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":380,\"stayTime\":22800000,\"goods\":760,\"workingCranes\":0,\"delay\":228},{\"arriveTime\":1615693074164,\"name\":\"c70be\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":403,\"stayTime\":24180000,\"goods\":806,\"workingCranes\":0,\"delay\":756},{\"arriveTime\":1615698233914,\"name\":\"e27ff\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":374,\"stayTime\":11220000,\"goods\":374,\"workingCranes\":0,\"delay\":1206},{\"arriveTime\":1615699143425,\"name\":\"3861f\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":595,\"stayTime\":35700000,\"goods\":1190,\"workingCranes\":0,\"delay\":1320},{\"arriveTime\":1615724873529,\"name\":\"01f58\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":425,\"stayTime\":25500000,\"goods\":850,\"workingCranes\":0,\"delay\":533},{\"arriveTime\":1615740222500,\"name\":\"c779a\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":528,\"stayTime\":7920000,\"goods\":264,\"workingCranes\":0,\"delay\":269},{\"arriveTime\":1615769928092,\"name\":\"38c12\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":729,\"stayTime\":21840000,\"goods\":729,\"workingCranes\":0,\"delay\":1433},{\"arriveTime\":1615775496019,\"name\":\"46186\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":271,\"stayTime\":16260000,\"goods\":542,\"workingCranes\":0,\"delay\":494},{\"arriveTime\":1615897056677,\"name\":\"ebe6b\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":820,\"stayTime\":24600000,\"goods\":820,\"workingCranes\":0,\"delay\":1018},{\"arriveTime\":1615916767873,\"name\":\"4fd66\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":479,\"stayTime\":7140000,\"goods\":239,\"workingCranes\":0,\"delay\":155},{\"arriveTime\":1615933703116,\"name\":\"802af\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":577,\"stayTime\":8640000,\"goods\":288,\"workingCranes\":0,\"delay\":542},{\"arriveTime\":1615945453379,\"name\":\"23de1\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":317,\"stayTime\":4740000,\"goods\":158,\"workingCranes\":0,\"delay\":540},{\"arriveTime\":1615949191979,\"name\":\"ee99b\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":101,\"stayTime\":1500000,\"goods\":50,\"workingCranes\":0,\"delay\":931},{\"arriveTime\":1615964277282,\"name\":\"e988a\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":511,\"stayTime\":30660000,\"goods\":1022,\"workingCranes\":0,\"delay\":1217},{\"arriveTime\":1615989405169,\"name\":\"99bf7\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":130,\"stayTime\":3900000,\"goods\":130,\"workingCranes\":0,\"delay\":1144},{\"arriveTime\":1616010448622,\"name\":\"ffa8c\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":57,\"stayTime\":3420000,\"goods\":114,\"workingCranes\":0,\"delay\":918},{\"arriveTime\":1616019525910,\"name\":\"4a672\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":508,\"stayTime\":30480000,\"goods\":1016,\"workingCranes\":0,\"delay\":264},{\"arriveTime\":1616029937797,\"name\":\"7a697\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":390,\"stayTime\":5820000,\"goods\":195,\"workingCranes\":0,\"delay\":424},{\"arriveTime\":1616030668609,\"name\":\"69ce4\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":515,\"stayTime\":7680000,\"goods\":257,\"workingCranes\":0,\"delay\":291},{\"arriveTime\":1616031912114,\"name\":\"81f30\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":823,\"stayTime\":12300000,\"goods\":411,\"workingCranes\":0,\"delay\":873},{\"arriveTime\":1616043672130,\"name\":\"61af4\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":188,\"stayTime\":11280000,\"goods\":376,\"workingCranes\":0,\"delay\":1094},{\"arriveTime\":1616239086740,\"name\":\"c737e\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":381,\"stayTime\":22860000,\"goods\":762,\"workingCranes\":0,\"delay\":1386},{\"arriveTime\":1616241912249,\"name\":\"9eba4\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":748,\"stayTime\":22440000,\"goods\":748,\"workingCranes\":0,\"delay\":544},{\"arriveTime\":1616621980160,\"name\":\"da31e\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":879,\"stayTime\":26340000,\"goods\":879,\"workingCranes\":0,\"delay\":754},{\"arriveTime\":1616640414333,\"name\":\"0fb9b\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":687,\"stayTime\":41220000,\"goods\":1374,\"workingCranes\":0,\"delay\":644},{\"arriveTime\":1616646055652,\"name\":\"d5cf7\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":27,\"stayTime\":1620000,\"goods\":54,\"workingCranes\":0,\"delay\":1426},{\"arriveTime\":1616765092560,\"name\":\"b8887\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":525,\"stayTime\":15720000,\"goods\":525,\"workingCranes\":0,\"delay\":561},{\"arriveTime\":1616794474550,\"name\":\"ad13e\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":625,\"stayTime\":9360000,\"goods\":312,\"workingCranes\":0,\"delay\":403},{\"arriveTime\":1616873900613,\"name\":\"90faa\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":217,\"stayTime\":13020000,\"goods\":434,\"workingCranes\":0,\"delay\":763},{\"arriveTime\":1616945007137,\"name\":\"1237e\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":645,\"stayTime\":9660000,\"goods\":322,\"workingCranes\":0,\"delay\":817},{\"arriveTime\":1616972906942,\"name\":\"4eaf6\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":956,\"stayTime\":28680000,\"goods\":956,\"workingCranes\":0,\"delay\":540},{\"arriveTime\":1616981001957,\"name\":\"44675\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":638,\"stayTime\":38280000,\"goods\":1276,\"workingCranes\":0,\"delay\":338},{\"arriveTime\":1616984079685,\"name\":\"47941\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":963,\"stayTime\":14400000,\"goods\":481,\"workingCranes\":0,\"delay\":369},{\"arriveTime\":1617060123008,\"name\":\"1ff6d\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":380,\"stayTime\":22800000,\"goods\":760,\"workingCranes\":0,\"delay\":341},{\"arriveTime\":1617075594047,\"name\":\"58e4b\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":279,\"stayTime\":8340000,\"goods\":279,\"workingCranes\":0,\"delay\":575},{\"arriveTime\":1617127132750,\"name\":\"6a262\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":770,\"stayTime\":23100000,\"goods\":770,\"workingCranes\":0,\"delay\":1066},{\"arriveTime\":1617163878451,\"name\":\"47a2f\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":17,\"stayTime\":1020000,\"goods\":34,\"workingCranes\":0,\"delay\":795},{\"arriveTime\":1617352061510,\"name\":\"2f8dc\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":827,\"stayTime\":12360000,\"goods\":413,\"workingCranes\":0,\"delay\":279},{\"arriveTime\":1617369625829,\"name\":\"30802\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":209,\"stayTime\":3120000,\"goods\":104,\"workingCranes\":0,\"delay\":1192},{\"arriveTime\":1617396284908,\"name\":\"b17a9\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":802,\"stayTime\":12000000,\"goods\":401,\"workingCranes\":0,\"delay\":795},{\"arriveTime\":1617626415542,\"name\":\"622c6\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":154,\"stayTime\":9240000,\"goods\":308,\"workingCranes\":0,\"delay\":153},{\"arriveTime\":1617636247844,\"name\":\"13c29\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":735,\"stayTime\":22020000,\"goods\":735,\"workingCranes\":0,\"delay\":195},{\"arriveTime\":1617725623154,\"name\":\"772da\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":45,\"stayTime\":660000,\"goods\":22,\"workingCranes\":0,\"delay\":126},{\"arriveTime\":1617776855266,\"name\":\"01178\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":151,\"stayTime\":9060000,\"goods\":302,\"workingCranes\":0,\"delay\":699},{\"arriveTime\":1617871256754,\"name\":\"68054\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":252,\"stayTime\":15120000,\"goods\":504,\"workingCranes\":0,\"delay\":598},{\"arriveTime\":1617903125861,\"name\":\"cf911\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":510,\"stayTime\":7620000,\"goods\":255,\"workingCranes\":0,\"delay\":1075},{\"arriveTime\":1617925940798,\"name\":\"56dc7\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":87,\"stayTime\":2580000,\"goods\":87,\"workingCranes\":0,\"delay\":512},{\"arriveTime\":1617979675157,\"name\":\"347ad\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":787,\"stayTime\":11760000,\"goods\":393,\"workingCranes\":0,\"delay\":516},{\"arriveTime\":1617985127940,\"name\":\"6b26e\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":232,\"stayTime\":6960000,\"goods\":232,\"workingCranes\":0,\"delay\":378},{\"arriveTime\":1617995144005,\"name\":\"8d2f3\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":353,\"stayTime\":5280000,\"goods\":176,\"workingCranes\":0,\"delay\":703},{\"arriveTime\":1618006601525,\"name\":\"65511\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":244,\"stayTime\":3660000,\"goods\":122,\"workingCranes\":0,\"delay\":456},{\"arriveTime\":1618073469260,\"name\":\"98609\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":535,\"stayTime\":16020000,\"goods\":535,\"workingCranes\":0,\"delay\":480},{\"arriveTime\":1618147578351,\"name\":\"b51f4\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":757,\"stayTime\":45420000,\"goods\":1514,\"workingCranes\":0,\"delay\":196},{\"arriveTime\":1618415787987,\"name\":\"4e5fd\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":572,\"stayTime\":34320000,\"goods\":1144,\"workingCranes\":0,\"delay\":814},{\"arriveTime\":1618432784974,\"name\":\"876c4\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":296,\"stayTime\":4440000,\"goods\":148,\"workingCranes\":0,\"delay\":594},{\"arriveTime\":1618499389971,\"name\":\"c6339\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":589,\"stayTime\":17640000,\"goods\":589,\"workingCranes\":0,\"delay\":236},{\"arriveTime\":1618523068375,\"name\":\"529e9\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":626,\"stayTime\":9360000,\"goods\":313,\"workingCranes\":0,\"delay\":609},{\"arriveTime\":1618575396176,\"name\":\"cc303\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":210,\"stayTime\":3120000,\"goods\":105,\"workingCranes\":0,\"delay\":362},{\"arriveTime\":1618647276190,\"name\":\"801c7\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":443,\"stayTime\":13260000,\"goods\":443,\"workingCranes\":0,\"delay\":915},{\"arriveTime\":1618670080859,\"name\":\"4a08e\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":351,\"stayTime\":5220000,\"goods\":175,\"workingCranes\":0,\"delay\":614},{\"arriveTime\":1618690481296,\"name\":\"f76fd\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":136,\"stayTime\":2040000,\"goods\":68,\"workingCranes\":0,\"delay\":222},{\"arriveTime\":1618792474341,\"name\":\"92a58\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":844,\"stayTime\":12660000,\"goods\":422,\"workingCranes\":0,\"delay\":108},{\"arriveTime\":1618799005915,\"name\":\"10d68\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":475,\"stayTime\":28500000,\"goods\":950,\"workingCranes\":0,\"delay\":264},{\"arriveTime\":1618803827139,\"name\":\"99ecf\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":252,\"stayTime\":7560000,\"goods\":252,\"workingCranes\":0,\"delay\":382},{\"arriveTime\":1618869991788,\"name\":\"4f397\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":821,\"stayTime\":12300000,\"goods\":410,\"workingCranes\":0,\"delay\":509},{\"arriveTime\":1618872526483,\"name\":\"21dea\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":237,\"stayTime\":14220000,\"goods\":474,\"workingCranes\":0,\"delay\":57},{\"arriveTime\":1618903194615,\"name\":\"18e44\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":237,\"stayTime\":3540000,\"goods\":118,\"workingCranes\":0,\"delay\":988},{\"arriveTime\":1618903616592,\"name\":\"5523e\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":68,\"stayTime\":4080000,\"goods\":136,\"workingCranes\":0,\"delay\":1275},{\"arriveTime\":1618923706259,\"name\":\"92455\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":595,\"stayTime\":35700000,\"goods\":1190,\"workingCranes\":0,\"delay\":348},{\"arriveTime\":1618929663908,\"name\":\"bfd03\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":333,\"stayTime\":19980000,\"goods\":666,\"workingCranes\":0,\"delay\":932},{\"arriveTime\":1619102462063,\"name\":\"b1156\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":456,\"stayTime\":13680000,\"goods\":456,\"workingCranes\":0,\"delay\":771},{\"arriveTime\":1619143202491,\"name\":\"347a1\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":635,\"stayTime\":38100000,\"goods\":1270,\"workingCranes\":0,\"delay\":976},{\"arriveTime\":1619210021057,\"name\":\"17451\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":67,\"stayTime\":1980000,\"goods\":67,\"workingCranes\":0,\"delay\":491},{\"arriveTime\":1619225348970,\"name\":\"b384d\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":792,\"stayTime\":47520000,\"goods\":1584,\"workingCranes\":0,\"delay\":1119},{\"arriveTime\":1619241669853,\"name\":\"56fd0\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":392,\"stayTime\":5880000,\"goods\":196,\"workingCranes\":0,\"delay\":1312},{\"arriveTime\":1619298406373,\"name\":\"4c6e0\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":881,\"stayTime\":52860000,\"goods\":1762,\"workingCranes\":0,\"delay\":1191},{\"arriveTime\":1619444592128,\"name\":\"d7a17\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":858,\"stayTime\":25740000,\"goods\":858,\"workingCranes\":0,\"delay\":1331},{\"arriveTime\":1619450996862,\"name\":\"50350\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":219,\"stayTime\":13140000,\"goods\":438,\"workingCranes\":0,\"delay\":1050},{\"arriveTime\":1619488746012,\"name\":\"9719e\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":840,\"stayTime\":12600000,\"goods\":420,\"workingCranes\":0,\"delay\":1431},{\"arriveTime\":1619495129534,\"name\":\"5c6da\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":945,\"stayTime\":28320000,\"goods\":945,\"workingCranes\":0,\"delay\":1141},{\"arriveTime\":1619512135949,\"name\":\"4de17\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":593,\"stayTime\":8880000,\"goods\":296,\"workingCranes\":0,\"delay\":416},{\"arriveTime\":1619575112913,\"name\":\"527e4\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":903,\"stayTime\":27060000,\"goods\":903,\"workingCranes\":0,\"delay\":483},{\"arriveTime\":1619619243243,\"name\":\"cc7e7\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":409,\"stayTime\":24540000,\"goods\":818,\"workingCranes\":0,\"delay\":447},{\"arriveTime\":1619850893998,\"name\":\"3a8c7\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":688,\"stayTime\":10320000,\"goods\":344,\"workingCranes\":0,\"delay\":382},{\"arriveTime\":1619855002572,\"name\":\"3d341\",\"type\":\"LOOSE\",\"weightType\":\"TONS\",\"weight\":755,\"stayTime\":45300000,\"goods\":1510,\"workingCranes\":0,\"delay\":1339},{\"arriveTime\":1620019946509,\"name\":\"0972a\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":658,\"stayTime\":19740000,\"goods\":658,\"workingCranes\":0,\"delay\":92},{\"arriveTime\":1620149639604,\"name\":\"81462\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":337,\"stayTime\":10080000,\"goods\":337,\"workingCranes\":0,\"delay\":1174},{\"arriveTime\":1620282105773,\"name\":\"39199\",\"type\":\"LIQUID\",\"weightType\":\"TONS\",\"weight\":143,\"stayTime\":2100000,\"goods\":71,\"workingCranes\":0,\"delay\":1182},{\"arriveTime\":1620360174644,\"name\":\"887f3\",\"type\":\"CONTAINER\",\"weightType\":\"PIECES\",\"weight\":824,\"stayTime\":24720000,\"goods\":824,\"workingCranes\":0,\"delay\":140}]\n";
//        JsonParser jsonParser = new JsonParser();
//        JsonElement obj = jsonParser.parse(json);
//        for (JsonElement elem : obj.getAsJsonArray()) {
//            timetable.add(new Gson().fromJson(elem.toString(), Ship.class));
//        }
//        Collections.sort(timetable);
//        timetable.forEach(System.out::println);

//        System.out.println("Добавить новый корабль? (д/н)");
//        Scanner sc = new Scanner(System.in);
//        if (sc.nextLine().toLowerCase().trim().equals("д")) {
//            Ship ship = addNewShip(sc);
//            System.out.println(ship);
//            timetable.add(ship);
//        }
//        Collections.sort(timetable);
        // PortController portController = new PortController(timetable, time);
        //portController.initPort();
        Operator<String> handleUserInput = scanner::nextLine;
        controller = new CommandController(handleUserInput, getCommandsHandlers());
        controller.start();
    }

    private static Operator[] getCommandsHandlers() {
        Operator<Void> generate = Program::generateNewTimetable;
        Operator<Void> show = Program::showTimetable;
        Operator<Void> add = Program::addNewShip;
        Operator<Void> start = Program::startSimulation;
        Operator<Void> stats = Program::showStatistic;
        Operator<Void> quit = Program::quit;
        return new Operator[]{generate, show, add, start, stats, quit};
    }

    private static Void generateNewTimetable() {
        ConsoleHandler.printMessage(TIMETABLE_GENERATE_START);
        timetable.clear();
        timetable.addAll(TimetableGenerator.generate(time));
        ConsoleHandler.printMessage(TIMETABLE_GENERATE_END);
        return null;
    }

    private static Void showTimetable() {
        System.out.println("Вывод расписания:\n");
        if (timetable.isEmpty()) {
            ConsoleHandler.printError(NO_TIMETABLE);
        }
        timetable.forEach(System.out::println);
        return null;
    }

    private static Void addNewShip() {
        System.out.println("Добавление нового корабля (оставьте поле пустым для случайного значения");
        System.out.println("Введите дату прибытия в формате дд.мм.год чч:мм: ");
        String date = scanner.nextLine();
        System.out.println("Выберите тип корабля (1. Сыпучий, 2. Жидкий, 3. Контейнер):");
        String type = scanner.nextLine();
        System.out.println("Введите вес: ");
        String weight = scanner.nextLine();
        System.out.println("Введите задержку: ");
        String delay = scanner.nextLine();
        try {
            Ship ship = TimetableGenerator.generateShip(time, date, type, weight, delay);
            System.out.println("Информация о добавленном корабле: " + ship);
            timetable.add(ship);
            ConsoleHandler.printMessage(SHIP_ADDED);
            Collections.sort(timetable);
        } catch (Exception e) {
            ConsoleHandler.printError(BAD_INPUT);
        }

        return null;
    }

    private static Void startSimulation() {
        if (timetable.isEmpty()) {
            ConsoleHandler.printError(NO_TIMETABLE);
        } else {
            controller.end();
            UnaryOperator<StatisticObject> onEnd = statisticObject -> {
                ConsoleHandler.printMessage(SIMULATION_END);
                lastStatistic = statisticObject;
                controller.start();
                return null;
            };
            ConsoleHandler.printMessage(SIMULATION_START);
            ConsoleHandler.setCyanColor();
            PortController portController = new PortController(timetable, time, onEnd);
            portController.initPort();
        }
        return null;
    }

    private static Void showStatistic() {
        if (lastStatistic != null) {
            System.out.println(lastStatistic);
        } else {
            ConsoleHandler.printError(NO_SIMULATIONS);
        }
        return null;
    }

    private static Void quit() {
        System.out.println("Подтверждаете выход из программы (+/-)?");
        if (scanner.nextLine().trim().toLowerCase().equals("+")) {
            controller.end();
        }
        return null;
    }

}
