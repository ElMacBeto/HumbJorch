package com.humbjorch.myapplication.sis.utils.util


object Constants {

    const val BASE_URL = "https://api.datos.gob.mx/"
    const val PHOTO_AUTHENTICATION_DEFAULT ="https://dthezntil550i.cloudfront.net/41/latest/411907122032322370009339024/1280_960/b8e5bfb1-11f7-487e-85cf-7fab328ea6aa.png"

    //END POINTS
    const val END_POINT_FACTS = "/v1/gobmx.facts"
    sealed class ListenerLoginDestination {
        object DestinationFirstTime : ListenerLoginDestination()
        object DestinationEmailAndPASSWORD : ListenerLoginDestination()
        object DestinationTouchId : ListenerLoginDestination()
        object DestinationGoogleSession : ListenerLoginDestination()
    }

}

