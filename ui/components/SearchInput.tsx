import {useState} from "react";

type SearchInputProps = {
    search: string,
    setSearch,
    submit,
}
const regex = new RegExp("[A-Za-z0-9]+=[A-Za-z0-9]+(,[A-Za-z0-9]+=[A-Za-z0-9]+)*")

export default function SearchInput({search, setSearch, submit}: SearchInputProps) {
    const [valid, setValid] = useState(null)

    const handleOnChange = function (event) {
        const val = event.target.value
        if(val.length > 0 && !regex.test(val)) {
            setValid(false)
        } else {
            setValid(true)
        }

        setSearch(val)
    }

    const handleOnSubmit = function () {
        if(!valid) {
            return
        }

        setValid(true)
        submit(search)
    }

    return (
        <div>
            <div className="d-flex mt-3 mb-3">
                <input type="text" className="form-control" placeholder="name=foo or tags=city" value={search} onChange={handleOnChange} />
                <button className="btn btn-primary ms-3" onClick={handleOnSubmit} disabled={!valid}>Search</button>
            </div>
            {valid == false && <span className="text-danger fs-6 fw-light">Invalid search pattern.</span>}
        </div>
    )
}