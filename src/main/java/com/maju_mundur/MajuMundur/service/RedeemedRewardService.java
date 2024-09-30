package com.maju_mundur.MajuMundur.service;

import com.maju_mundur.MajuMundur.dto.Request.RedeemRequest;
import com.maju_mundur.MajuMundur.dto.Response.RedeemResponse;

public interface RedeemedRewardService {
    RedeemResponse redeemPoint(RedeemRequest redeemRequest);
}
