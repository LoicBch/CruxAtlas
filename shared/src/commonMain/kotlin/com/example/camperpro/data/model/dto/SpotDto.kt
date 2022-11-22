package com.example.camperpro.data.model.dto

import com.jetbrains.kmm.shared.data.model.dto.PhotoDto
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SpotDto(
    var id: String? = null,
    var latitude: String = "",
    var longitude: String = "",
    var titre: String = "",
    var name: String? = null,

    @SerialName("description_fr")
    var descriptionFr: String? = null,

    @SerialName("description_en")
    var descriptionEn: String? = null,

    @SerialName("description_de")
    var descriptionDe: String? = null,

    @SerialName("description_it")
    var descriptionIt: String? = null,

    @SerialName("description_nl")
    var descriptionNl: String? = null,

    @SerialName("description_es")
    var descriptionEs: String? = null,
    var reseaux: String? = null,

    @SerialName("date_fermeture")
    var dateFermeture: String? = null,
    var borne: String? = null,

    @SerialName("prix_stationnement")
    var prixStationnement: String? = null,

    @SerialName("prix_services")
    var prixServices: String? = null,

    @SerialName("nb_places")
    var nbPlaces: String? = null,

    @SerialName("hauteur_limite")
    var hauteurLimite: String? = null,
    var caravaneige: String? = null,
    var electricite: String? = null,
    var douche: String? = null,
    var poubelle: String? = null,
    var animaux: String? = null,

    @SerialName("eau_noire")
    var eauNoire: String? = null,

    @SerialName("eau_usee")
    var eauUsee: String? = null,
    var boulangerie: String? = null,

    @SerialName("point_eau")
    var pointEau: String? = null,
    var visites: String? = null,

    @SerialName("point_de_vue")
    var pointDeVue: String? = null,
    var moto: String? = null,
    var baignade: String? = null,
    var piscine: String? = null,
    var laverie: String? = null,

    @SerialName("jeux_enfants")
    var jeuxEnfants: String? = null,
    var lavage: String? = null,
    var gpl: String? = null,
    var gaz: String? = null,

    @SerialName("donnees_mobile")
    var donneesMobile: String? = null,
    var windsurf: String? = null,
    var vtt: String? = null,
    var rando: String? = null,

    @SerialName("wc_public")
    var wcPublic: String? = null,
    var escalade: String? = null,

    @SerialName("eaux_vives")
    var eauxVives: String? = null,
    var wifi: String? = null,
    var peche: String? = null,

    @SerialName("peche_pied")
    var pechePied: String? = null,

    @SerialName("date_creation")
    var dateCreation: String? = null,
    var route: String? = null,
    var ville: String? = null,
    var publique: Int? = null,

    @SerialName("contact_visible")
    var contactVisible: Int? = null,

    @SerialName("top_liste")
    var topListe: Int? = null,

    @SerialName("site_internet")
    var siteInternet: String? = null,
    var video: String? = null,
    var tel: String? = null,
    var mail: String? = null,

    @SerialName("code_postal")
    var codePostal: String? = null,
    var pays: String? = null,

    @SerialName("pays_iso")
    var paysIso: String? = null,
    var code: String? = null,

    @SerialName("utilisateur_creation")
    var utilisateurCreation: String? = null,

    @SerialName("user_id")
    var userId: String? = null,

    @SerialName("user_vehicule")
    var userVehicule: String? = null,

    @SerialName("note_moyenne")
    var noteMoyenne: String? = null,

    @SerialName("nb_commentaires")
    var nbCommentaires: String? = null,

    @SerialName("nb_visites")
    var nbVisites: String? = null,

    @SerialName("nb_photos")
    var nbPhotos: String? = null,

    @SerialName("validation_admin")
    var validationAdmin: String? = null,
    var distance: String? = null,
    var photos: ArrayList<PhotoDto> = arrayListOf()
)
