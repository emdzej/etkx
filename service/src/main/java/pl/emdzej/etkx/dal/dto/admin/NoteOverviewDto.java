package pl.emdzej.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteOverviewDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String benennung;
    private String monat;
    private String jahr;
}
