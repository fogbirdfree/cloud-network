package com.dongruan.graduation.networkdiskapi.service;

import com.dongruan.graduation.networkdiskcommon.param.CheckDirWhetherParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateDirParam;
import com.dongruan.graduation.networkdiskcommon.param.CreateVirtualAddressParam;
import com.dongruan.graduation.networkdiskcommon.response.VirtualAddressDTO;
import com.dongruan.graduation.networkdiskcommon.utils.RestResult;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CoreService {
    @PostMapping(value = "core/createDir")
    RestResult<String> createDir(@RequestBody CreateDirParam request);

    @GetMapping(value = "core/checkDirWhether")
    RestResult<Integer> checkDirWhether(@SpringQueryMap CheckDirWhetherParam request);

    @PostMapping(value = "core/createVirtualAddress")
    RestResult<Integer> createVirtualAddress(@RequestBody CreateVirtualAddressParam request);

    @GetMapping(value = "core/getFilenameByVid")
    RestResult<String> getFilenameByVid(@RequestParam("vid") String vid);

    @GetMapping(value = "core/getVirtualAddress")
    RestResult<VirtualAddressDTO> getVirtualaddress(@RequestParam("vid") String vid, @RequestParam("uid") String uid);

    @PostMapping(value = "core/initCapacity")
    RestResult<Integer> initCapacity(@RequestParam("userId") String userId);
}
