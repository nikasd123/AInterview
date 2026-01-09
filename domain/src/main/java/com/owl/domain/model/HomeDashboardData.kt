package com.owl.domain.model

data class HomeDashboardData(
    val sessions: List<InterviewSession>,
    val averageScore: Int,
    val completedCount: Int,
    val topSkill: String
)