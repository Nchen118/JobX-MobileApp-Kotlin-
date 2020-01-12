package com.example.jobx.database

data class Job (

    var job_id: String?= null,
    var company_id:String? = null,
    var job_name:String? = null,
    var job_desc:String? = null,
    var job_number:Int? = null,
    var job_location:String? = null,
    var job_position:String? = null,
    var job_salary:Float? = null,
    var job_date:String? = null,
    var job_status:String? = null
)