package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.util.Random

/**
  * Created by darkg on 28-May-17.
  */
class SwearShortlistController @Inject() extends Controller {

  private val swears = List("Fututi mortii, ranitii si arhanghelii matii!", "Pizda ma-tii de boul pulii mele facut din laba trista si care nu stii sa conduci", "Trag pula-n ea de treaba", "Cacama's bucati de pula la ma'ta'n gura", "Futu-ți morții ma-tii", "Ești handicapat cu loc de parcare", "Futu-ți mormânții mă-tii", "Grijania ma-tii!! ", "Să-mi bag pula-n tine de sclav(ă)", "Facemi-as schiuri din crucea ma-tii", "Sa te fut in nara", "futu-ți America și nația mă-tii", "Futu-ti nașterea și parastasu matii ", "Sa ma fut in cur cu clanta", "Tuți cristelnița mă tii", "Futu-ti dumnezeii matii.", "Treci, da-te-n plua mea!", "Te misti ca soacra-mea cand pleaca!", "Fute-te-ar Dumniezo!", "Da-ti-ar radioul numai Nickelback", "Baga-mi-as pula in semnalizarea ta", "Mortii matii", "Baga-mi-as pula in el taximetrist idiot", "Fututi cristosu matii", "Futu-ti mama ma-tii !!!!", "Futu-ti lampa ma-tii de idiot!", "Puii mei", "bag pula in telefonu matii ", "Ce pula mea", "Pizda ma-tii!", "Manca-v-ar raiul", "Bagati-as pula-n inima sa ma ai la suflet", "defeca-m-as in vaginul mamicutei tale cu bucati de penis intregi", "Du-te dracu de primata fara creier.", "Fututi morții matiiii de căcat vestejit cu ochi !!!", "Futu-ti fanina ma-tii", "În pula calului", "Băga-mi-aș pula în inima ta", "Baga-mi-as pula peste ma-ta in casa!", "Să îmi sugi pula din mână !", "Ce faci coaie crețe?", "Futu-ti Zebedeul Ma-ti!", "Să îți iau neamu' în pulă de prost", "Fuck your mothers onion !", "Unde-i  să mă vadă, să-i dau pula să mi-o roadă :))", "Belimeai mădularu'", "La voi în CS e opțională semnalizarea ?", "Sa ii dau muie la mă-ta cu zahar pana face diabet.", "Mă-ta nu te-a învățat să te uiți în oglinzi?", "SĂ TE FUT ÎN INIMĂ", "drace ia-te ", "Fosu calului", "Ma-ta", "Băga-ți-ai pula-n el de semafor inteligent!", "Ceapa mă-tii de kkt cu bemveu!", "Băgați-ai telefonu-n cur de proastă!", "Gura mătii de primar cu coada ta de mașini!", "Trăgia-mi-aș ciorapii prin colvia ta de hârciog!", "Ce pula mea e o injuratoru? ", "Futu-m-ar Dumnezeu", "Luați-aș viitorul copiilor tăi în pulă. Aa. Stai, copii tăi nu au viitor, muist inpropriu. Căutător de călăi pentru sinuciderea întregi tale familii.", "Ma-ta", "Dumnezaii matii", "Sa il/o futa dracu in gura si-n cur", "Tu-ți ceapa matii!", "Dumnezei mati", "sa o fut pe ma-ta moarta cu cacat in gura", "sa te caci in pizda la mă-tă", "sa te fută Dumnezo cu grebla cu care strânge Sf. Petru stelele de pe cer dimineața", "sugi pula gârlău borât cu ma ta aia curva", "In pizda matii de mitocan", "In pizda matii de mitocan", "Futa-te luna de cretin", "Du-te-n pula mea", "Târâ-mi-aș coaiele-n coliva mă-tii din bomboană în bomboană, în sens giratoriu, până la lumânare și înapoi!", "Sa iti fut mortii matii de taran obosit ceri injuraturi pe un grup de organizatie", "Futa-va unu pa altu!", "Te bag in mata, de-a latu  !", "Da-o-ai in boala cainelui !", "Sa-ti sparg dintii !", "Complimente lu Mata !", "Sa te taie doftorii !", "Sa mori in slam ! (un fel de var stins) .", "Du-te-n ma-ta (boule)", "Du-te-n pula mea", "Prostu' Puli", "Muia-mi-as cornu in laptele ma-ti!", "Tu-ti gatu ma-tii", "Baga-mi-as pula", "Caca-ma-s pe viata ta!", "Futuzi crucea mati!", "Sa o f... pe ma-ta!", "Boule!/Vaco!", "Du-te-n p... ma-tii!", "Du-te-n p... calului!", "Idiotule", "Cine dracu ti-o mai dat si tie permisu?", "Fututi mortii matii", "Prostu' pulii mele", "Faceamias schiuri din crucea masii aluia care ti-a dat permisul", "Rupe-si-ar haturile caii lui Dumnezeu incercand sa imi scoata pula din mortii lu' ma-ta", "Schiloada dracului! ", "Dobitocu matii pula sa sugi ", "Mult Furazolidon si No-Spa primarului!", "Lua-ți-aș falca-n pulă!", "Futu-ti Dumnezeii matii")

  def getSwears = Action.async {
    implicit request =>

      Future {
        val swearsResult = cleanSwears(List(swears(new Random().nextInt(swears.length)),
          swears(new Random().nextInt(swears.length)),
          swears(new Random().nextInt(swears.length))))

        Ok(Json.toJson(swearsResult))
      }
  }

  def cleanSwears(swearList: List[String]): List[String] = {
    swearList.map {
      sw =>
        sw.toLowerCase.replaceAll("pula", "***")
          .replaceAll("pizda", "****")
          .replaceAll("pizdă", "****")
          .replaceAll("futa", "***")
          .replaceAll("fută", "***")
          .replaceAll("pulă", "***")
          .replaceAll("puli", "***")
          .replaceAll("muie", "***")
          .replaceAll("fut", "***")
          .replaceAll("cacat", "*****")
          .replaceAll("căcat", "****")
          .replaceAll("căcat", "****")
          .replaceAll("caca", "*****")
          .replaceAll("coaiele", "****")
          .replaceAll("coaie", "****")
    }
  }

}
